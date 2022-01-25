package cqrs.app.domain.model.base

import cqrs.app.domain.infra.Event
import cqrs.app.domain.infra.Message
import java.util.*

abstract class AggregateRoot(val id: UUID, events: List<Event>) {
    private val changes:MutableList<Event> = mutableListOf()
    val version:Int

    init {
        loadHistory(events)
        version = events.size
    }

    private fun loadHistory(events: List<Event>) {
        events.forEach { applyChange(it, isNew = false); }
    }

    fun changes():List<Event> {
       return changes.toList()
    }

    fun changesAndClear():List<Event> {
        val events = changes.toList()
        changes.clear()
        return events
    }

    protected fun <T : Event> applyChange(
        event: T,
        isNew: Boolean = true
    ) {
        dispatch(event)
        if(isNew) changes.add(event)
    }

//    abstract fun <T : Event> dispatch(event: T)

    private fun <T : Event> dispatch(event: T) {
        val eventMethod = this::class.java.getDeclaredMethod(
            "eventToState",
            event::class.java
        )
        eventMethod.isAccessible = true;
        eventMethod.invoke(this, event)
        eventMethod.isAccessible = false;
    }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as AggregateRoot
        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

}

interface Repository<A: AggregateRoot>{
    suspend fun getBy(id: UUID): A
    suspend fun save(a: A)
}


