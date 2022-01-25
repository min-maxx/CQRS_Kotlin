package cqrs.app.domain.model.member

import cqrs.app.domain.model.member.event.MemberEmailValidated
import cqrs.app.domain.model.member.event.MemberValidationExtended
import cqrs.app.domain.model.member.event.MemberSignedUp
import cqrs.app.domain.model.member.event.PasswordChanged
import cqrs.app.domain.model.base.AggregateRoot
import cqrs.app.domain.infra.Event
import java.time.LocalDate
import java.util.*


@Suppress("ProtectedInFinal")
class Member(
    id: UUID,
    events: List<Event> = emptyList()
) : AggregateRoot(id, events) {
    private var emailValidated: Boolean = false
    private lateinit var validationExpirationDate: LocalDate
    private lateinit var passwordHash: String

    constructor(
        id: UUID,
        email: Email,
        password: Password,
        name: String,
        age: Age,
        date: LocalDate
    ) : this(id) {
        require(name.isNotBlank()) { "Name is missing" }
        val validationExpirationDate = date.plusDays(15)
        applyChange(
            MemberSignedUp(
                id,
                name,
                email.address,
                password.hash,
                age.value,
                date,
                validationExpirationDate,
                emailValidated
            )
        )
    }

    @Suppress("unused")
    private fun eventToState(e: MemberSignedUp) { //run in différent context, not allow to say 'no'
        this.validationExpirationDate = e.validationExpirationDate
        this.passwordHash = e.passwordHash
        // no need to add all args as it is not used as invariant in any rule
    }

    fun validate(validationDate: LocalDate) {
        require(!emailValidated) { "Member email is already validated" }
        if (validationDate.isAfter(validationExpirationDate)) {
            val validationExpirationDate = validationDate.plusDays(15)
            applyChange(MemberValidationExtended(id, validationExpirationDate))
        } else {
            applyChange(MemberEmailValidated(id))
        }
    }

    @Suppress("unused")
    private fun eventToState(e: MemberValidationExtended) {
        validationExpirationDate = e.newValidationExpirationDate
    }

    @Suppress("unused")
    private fun eventToState(e: MemberEmailValidated) { //run in différent context, not allow to say 'no'
        // always x=y, never if
        emailValidated = true
        // no need to ad comment as it is not used as invariant in any rule
    }

    fun changePassword(old: Password, new: Password) {
        require(old.hash == passwordHash) { "Old Password is not correct" }
        require(new.hash != passwordHash) { "Password must be différent" }
        applyChange(PasswordChanged(id, new.hash))
    }

    @Suppress("unused")
    private fun eventToState(e: PasswordChanged) {
        this.passwordHash = e.passwordHash
    }

//    override fun <T : Event> dispatch(event: T) {
//        when (event) {
//            is MemberSignedUp -> applyEvent(event)
//            is MemberEmailValidated -> applyEvent(event)
//            is PasswordChanged -> applyEvent(event)
//        }
//    }

}

