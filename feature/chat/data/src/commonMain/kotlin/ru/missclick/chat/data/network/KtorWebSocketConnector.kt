@file:OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)

package ru.missclick.chat.data.network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.header
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import ru.missclick.chat.data.dto.websocket.WebSocketMessageDto
import ru.missclick.chat.data.lifecycle.AppLifecycleObserver
import ru.missclick.chat.domain.models.ConnectionState
import ru.missclick.core.data.networking.UrlConstants.BASE_URL_WS
import ru.missclick.core.domain.auth.SessionStorage
import ru.missclick.core.domain.logging.CourseLogger
import ru.missclick.core.domain.util.DataError
import ru.missclick.core.domain.util.EmptyResult
import ru.missclick.core.domain.util.Result
import ru.missclick.feature.chat.data.BuildKonfig
import kotlin.time.Duration.Companion.seconds

class KtorWebSocketConnector(
    private val httpClient: HttpClient,
    private val applicationScope: CoroutineScope,
    private val sessionStorage: SessionStorage,
    private val json: Json,
    private val connectionErrorHandler: ConnectionErrorHandler,
    private val connectionRetryHandler: ConnectionRetryHandler,
    private val appLifecycleObserver: AppLifecycleObserver,
    private val connectivityObserver: ConnectivityObserver,
    private val logger: CourseLogger
) {
    private val _connectionState = MutableStateFlow(ConnectionState.DISCONNECTED)
    val connectionState = _connectionState.asStateFlow()

    private var currentSession: WebSocketSession? = null

    private val isConnected = connectivityObserver
        .isConnected
        .debounce(1.seconds)
        .stateIn(
            applicationScope,
            SharingStarted.WhileSubscribed(5000L),
            false
        )

    private val isInForeground = appLifecycleObserver
        .isInForeground
        .onEach { isInForeground ->
            if (isInForeground) {
                connectionRetryHandler.resetDelay()
            }
        }
        .stateIn(
            applicationScope,
            SharingStarted.WhileSubscribed(5000L),
            false
        )

    val messages = combine(
        sessionStorage.observeAuthInfo(),
        isConnected,
        isInForeground
    ) { authInfo, isConnected, isInForeground ->
        when {
            authInfo == null -> {
                logger.info("No authentication details. Clearing session and disconnecting...")
                _connectionState.update { ConnectionState.DISCONNECTED }
                currentSession?.close()
                currentSession = null
                connectionRetryHandler.resetDelay()
                null
            }
            !isInForeground -> {
                logger.info("App in background, disconnecting socket proactively")
                _connectionState.update { ConnectionState.DISCONNECTED }
                currentSession?.close()
                currentSession = null
                null
            }
            !isConnected -> {
                logger.info("Device is disconnected, closing WebSocket connection")
                _connectionState.update { ConnectionState.ERROR_NETWORK }
                currentSession?.close()
                currentSession = null
                null
            }
            else -> {
                logger.info("App in foreground and connected. Establishing connection")

                if (_connectionState.value !in listOf(
                        ConnectionState.CONNECTING,
                        ConnectionState.CONNECTED
                    )
                ) {
                    _connectionState.update { ConnectionState.CONNECTING }
                }

                authInfo
            }
        }
    }.flatMapLatest { authInfo ->
        if (authInfo == null) {
            emptyFlow()
        } else {
            createWebSocketFlow(accessToken = authInfo.accessToken)
                .catch { e ->
                    logger.error("Exception in websocket", e)

                    currentSession?.close()
                    currentSession = null
                    val transformedException = connectionErrorHandler.transformException(e)
                    throw transformedException
                }
                .retryWhen { cause, attempt ->
                    logger.info("Connection failed on attempt $attempt")

                    val shouldRetry = connectionRetryHandler.shouldRetry(cause, attempt)

                    if (shouldRetry) {
                        _connectionState.update { ConnectionState.CONNECTING }
                        connectionRetryHandler.applyRetryDelay(attempt)
                    }

                    shouldRetry
                }
                .catch { e ->
                    logger.error("Unhandled WebSocket error", e)
                    _connectionState.update { connectionErrorHandler.getConnectionStateForError(e) }
                }
        }
    }

    private fun createWebSocketFlow(accessToken: String) = callbackFlow {
        _connectionState.update { ConnectionState.CONNECTING }

        currentSession = httpClient.webSocketSession(
            urlString = "$BASE_URL_WS/chat"
        ) {
            header("Authorization", "Bearer $accessToken")
            header("X-API-KEY", BuildKonfig.API_KEY)
        }

        currentSession?.let { session ->
            _connectionState.update { ConnectionState.CONNECTED }

            session
                .incoming
                .consumeAsFlow()
                .buffer(
                    capacity = 100
                )
                .collect { frame ->
                    when (frame) {
                        is Frame.Text -> {
                            val text = frame.readText()
                            logger.info("Received raw text frame: $text")

                            val messageDto = json.decodeFromString<WebSocketMessageDto>(text)
                            send(messageDto)
                        }
                        is Frame.Ping -> {
                            logger.debug("Received ping from server. Sending pong...")
                            session.send(Frame.Pong(frame.data))
                        }
                        else -> Unit
                    }
                }
        } ?: throw Exception("Failed to establish WebSocket conneciton")

        awaitClose {
            launch(NonCancellable) {
                logger.info("Disconnecting from WebSocket session...")
                _connectionState.update { ConnectionState.DISCONNECTED }
                currentSession?.close()
                currentSession = null
            }
        }
    }

    suspend fun sendMessage(message: String): EmptyResult<DataError.Connection> {
        val connectionState = connectionState.value

        if (currentSession == null || connectionState != ConnectionState.CONNECTED) {
            return Result.Failure(DataError.Connection.NOT_CONNECTED)
        }

        return try {
            currentSession?.send(message)
            Result.Success(Unit)
        } catch (e: Exception) {
            currentCoroutineContext().ensureActive()
            logger.error("Unable to send WebSocket message", e)
            Result.Failure(DataError.Connection.MESSAGE_SEND_FAILED)
        }
    }
}