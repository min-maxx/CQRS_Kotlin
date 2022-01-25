package cqrs.app.domain.infra.bus

import cqrs.app.domain.infra.Event
import cqrs.app.domain.infra.Handles


open class EventBus {
    private val handlers = mutableMapOf<String, MutableList<Handles<Event>>>()

    @Suppress("NON_PUBLIC_CALL_FROM_PUBLIC_INLINE")
    inline fun <reified T : Event> register(handler: Handles<T>) {
        val type = T::class.java.typeName
        val eventHandlers = handlers[type] ?: mutableListOf()
        eventHandlers.add(handler as Handles<Event>)
        handlers[type] = eventHandlers
    }

    suspend fun publish(message: Event) {
        val type = message::class.java.typeName
        if (!handlers.containsKey(type))
            println("no handlers found for type $type")
        val eventHandlers = handlers[type] ?: emptyList()
        eventHandlers.forEach {
            try { it.handle(message) }
            catch (e: Exception) { println("Error ...") }
        }
    }
}