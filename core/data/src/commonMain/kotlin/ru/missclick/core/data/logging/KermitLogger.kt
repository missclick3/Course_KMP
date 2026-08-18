package ru.missclick.core.data.logging

import co.touchlab.kermit.Logger
import ru.missclick.core.domain.logging.CourseLogger

object KermitLogger : CourseLogger {
    override fun info(message: String) {
        Logger.i(message)
    }

    override fun debug(message: String) {
        Logger.d(message)
    }

    override fun warn(message: String) {
        Logger.w(message)
    }

    override fun error(message: String, throwable: Throwable?) {
        Logger.e(message, throwable)
    }
}