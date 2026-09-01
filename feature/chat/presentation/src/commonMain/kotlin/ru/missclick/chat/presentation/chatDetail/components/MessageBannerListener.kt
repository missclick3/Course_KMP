package ru.missclick.chat.presentation.chatDetail.components

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import ru.missclick.chat.presentation.model.MessageUi
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun MessageBannerListener(
    lazyListState: LazyListState,
    messages: List<MessageUi>,
    isBannerVisible: Boolean,
    onShowBanner: (topVisibleItemIndex: Int) -> Unit,
    onHide: () -> Unit
) {
    val isBannerVisibleUpdated by rememberUpdatedState(isBannerVisible)

    LaunchedEffect(messages) {
        snapshotFlow {
            val info = lazyListState.layoutInfo
            val visibleItems = info.visibleItemsInfo
            val total = info.totalItemsCount

            val oldestVisibleMessageIndex = visibleItems.maxOfOrNull { it.index } ?: -1

            val isAtOldestMessages = oldestVisibleMessageIndex >= total - 1
            val isAtNewestMessages = visibleItems.any { it.index == 0 }

            MessageBannerScrollState(
                oldestVisibleMessageIndex = oldestVisibleMessageIndex,
                isScrollInProgress = lazyListState.isScrollInProgress,
                isAtEdgeOfList = isAtNewestMessages || isAtOldestMessages
            )
        }
            .distinctUntilChanged()
            .collect {  (oldestVisibleMessageIndex, isScrollInProgress, isAtEdgeOfList) ->
                val shouldShowBanner = isScrollInProgress &&
                        !isAtEdgeOfList &&
                        oldestVisibleMessageIndex >= 0

                when {
                    shouldShowBanner -> onShowBanner(oldestVisibleMessageIndex)
                    !shouldShowBanner && isBannerVisibleUpdated -> {
                        delay(1000.milliseconds)
                        onHide()
                    }
                }
            }
    }
}

data class MessageBannerScrollState(
    val oldestVisibleMessageIndex: Int,
    val isScrollInProgress: Boolean,
    val isAtEdgeOfList: Boolean
)