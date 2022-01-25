package cqrs.app.domain.command.cross

import cqrs.app.domain.infra.Handles
import cqrs.app.domain.infra.Message
import java.util.*
import java.util.logging.ConsoleHandler
import java.util.logging.Level
import java.util.logging.LogRecord
import java.util.logging.Logger

class WithLogging<T : Message>(val next: Handles<T>) : Handles<T> {
    private val logger = Logger.getLogger(next::class.simpleName).apply {
        handlers.forEach { removeHandler(it) }
        addHandler(ConsoleHandler().apply {
            formatter = MyFormatter()
            level = Level.ALL
        })
        level = Level.FINE
    }

    override suspend fun handle(message: T) {
        logger.finer("Handling message=$message")
        try {
            next.handle(message)
            logger.fine("Handled message=$message")
        } catch (e: Exception) {
            logger.log(Level.SEVERE, "Error occurred when handling message=$message", e)
            throw e
        }
    }
}

class MyFormatter : java.util.logging.Formatter() {
    override fun format(record: LogRecord): String {
        return (Date(record.millis).toString() + ":"
                + record.longThreadID.toString() + ":"
                + record.level + ":"
                + record.loggerName + "."
                + record.sourceMethodName + " "
                + record.message + "\n")
    }
}