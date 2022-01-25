package cqrs.app.domain.infra.repositories

import cqrs.app.domain.model.base.AggregateRoot
import cqrs.app.domain.model.base.Repository
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class EventStoreRepository<A : AggregateRoot> private constructor(
    private val type: KClass<A>,
    private val eventStore: EventStore
) : Repository<A> {
    companion object {
        @Suppress("NON_PUBLIC_CALL_FROM_PUBLIC_INLINE")
        inline operator fun <reified A : AggregateRoot> invoke(
            eventStore: EventStore
        ) = EventStoreRepository(A::class, eventStore)
    }

    override suspend fun getBy(id: UUID): A {
        val events = eventStore.getEventsBy(id)
        return type.primaryConstructor!!.call(id, events)
    }

    override suspend fun save(a: A) {
        eventStore.saveAndPublishEvents(
            a.changesAndClear(),
            a.id,
            a.version
        )
    }

}