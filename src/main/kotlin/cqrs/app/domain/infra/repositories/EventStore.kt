package cqrs.app.domain.infra.repositories

import com.fasterxml.jackson.databind.ObjectMapper
import cqrs.app.domain.infra.Event
import cqrs.app.domain.infra.bus.EventBus
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.util.*


interface EventStore {
    suspend fun getEventsBy(id: UUID): List<Event>
    suspend fun saveAndPublishEvents(changes: List<Event>, id: UUID, version: Int): List<Event>
}

data class EventMetaData(
    val aggregateId: UUID,
    val date: LocalDateTime,
    val type: Class<out Event>,
    val json: String
)

class InMemoryEventStore(
    private val eventBus: EventBus,
    private val jsonMapper: ObjectMapper
) : EventStore {
    private val memorisedEvents = mutableMapOf<UUID, MutableList<EventMetaData>>()

    override suspend fun getEventsBy(id: UUID): List<Event> {
        val list: List<Event> = memorisedEvents[id]
            ?.map { jsonMapper.readValue(it.json, it.type) }
            ?: emptyList()
        delay(200) //IO latency
        return list
    }

    override suspend fun saveAndPublishEvents(
        changes: List<Event>,
        id: UUID,
        expectedVersion: Int
    ): List<Event> {
        val eventList: MutableList<EventMetaData> = memorisedEvents.getOrDefault(id, mutableListOf())
        val currentVersion: Int = eventList.size
        require(expectedVersion == currentVersion) { "Error on version ..." }
        changes.forEach {
            val json = jsonMapper.writeValueAsString(it)
            eventList.add(
                EventMetaData(
                    id,
                    LocalDateTime.now(),
                    it::class.java,
                    json
                )
            )
            delay(100) //IO latency
        }
        changes.forEach {
            eventBus.publish(it) //4
        }
        memorisedEvents[id] = eventList
        return changes
    }

}
