package cqrs.app.domain.membership.model

import cqrs.app.domain.model.member.Age
import cqrs.app.domain.model.member.Email
import cqrs.app.domain.model.member.Member
import cqrs.app.domain.model.member.event.MemberEmailValidated
import cqrs.app.domain.model.member.event.MemberSignedUp
import cqrs.app.domain.model.member.Password
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.Month
import java.util.*
import kotlin.apply

class MemberTest {
    val uuid = UUID.randomUUID()
    @Test
    fun `should have MemberSignedUp when I create new Member`() { // test are event centric
        val changes = Member(uuid,  Email("maxx@email.com"), Password("123456"),"Maxx", Age(23), LocalDate.of(2022, Month.APRIL, 14)).run {
            changes()
        }

        changes shouldHaveSize 1
        changes.first() shouldBe MemberSignedUp(uuid,  "Maxx","maxx@email.com", "1450575459",23, LocalDate.of(2022, Month.APRIL, 14), LocalDate.of(2022, Month.APRIL, 29), false)
    }

    @Test
    fun `should have ItemDeactivated event when I deactivate item`() { // test are event centric
        val changes = Member(uuid,  Email("maxx@email.com"), Password("123456"),"Maxx", Age(23), LocalDate.of(2022, Month.MARCH, 31)).run {
            validate(LocalDate.of(2022, Month.APRIL, 14))
            changes()
        }

        changes shouldHaveSize 2
        changes.last() shouldBe MemberEmailValidated(uuid)
    }

    @Test
    fun `should have Exception event when I deactivate item twice `() { // test are event centric
        val member = Member(uuid,  Email("maxx@email.com"), Password("123456"),"Maxx", Age(23), LocalDate.of(2022, Month.MARCH, 31)).apply {
            validate(LocalDate.of(2022, Month.APRIL, 14))
        }

        shouldThrow<IllegalArgumentException> { member.validate(LocalDate.of(2022, Month.APRIL, 15)) }

        val changes = member.changes()

        changes shouldHaveSize 2
        changes.last() shouldBe MemberEmailValidated(uuid)
    }
}