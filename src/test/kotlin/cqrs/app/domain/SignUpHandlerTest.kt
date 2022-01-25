package cqrs.app.domain

import cqrs.app.domain.command.SignUp
import cqrs.app.domain.command.SignUpHandler
import cqrs.app.domain.infra.repositories.EventStoreRepository
import cqrs.app.domain.model.member.Member
import cqrs.app.domain.model.member.event.MemberSignedUp
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldHaveSize
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.Month
import java.util.*

internal class SignUpHandlerTest{
    private val eventStoreMock = MockEventStore()
    private val authenticationServiceMock = MockAuthenticationService()
    private val repository = EventStoreRepository<Member>(eventStoreMock)


    @Test
    fun `should handle SignUp message`() { // test are event centric
        val handler = SignUpHandler(repository, authenticationServiceMock)
        val uuid = UUID.randomUUID()
        val commandMessage = SignUp(uuid, "Maxx", "maxx@email.com", "123456", 23, LocalDate.of(2022, Month.APRIL, 12))

        runBlocking {  handler.handle(commandMessage) }

        eventStoreMock.memorisedEvents shouldHaveSize 1
        eventStoreMock.memorisedEvents shouldContain  MemberSignedUp(uuid, "Maxx", "maxx@email.com", "1450575459", 23, LocalDate.of(2022, Month.APRIL, 12), LocalDate.of(2022, Month.APRIL, 27), false)
    }
}


