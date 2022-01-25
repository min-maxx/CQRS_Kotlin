package cqrs.app.domain

import cqrs.app.domain.infra.Event
import cqrs.app.domain.infra.repositories.EventStore
import cqrs.app.domain.model.member.AuthenticationService
import java.util.*

class MockEventStore : EventStore {
    val memorisedEvents: MutableList<Event> = mutableListOf()

    override suspend fun getEventsBy(id: UUID): List<Event> {
        return memorisedEvents.filter { it.id == id }
    }

    override suspend fun saveAndPublishEvents(changes: List<Event>, id: UUID, version: Int): List<Event> {
        memorisedEvents.addAll(changes)
        return changes
    }

}

class MockAuthenticationService(val check: Boolean = false) : AuthenticationService {
    lateinit var email: String
    override suspend fun checkExist(email: String): Boolean {
        this.email =email
        return  check
    }

}