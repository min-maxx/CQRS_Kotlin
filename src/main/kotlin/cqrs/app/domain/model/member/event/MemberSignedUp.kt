package cqrs.app.domain.model.member.event

import cqrs.app.domain.infra.Event
import java.time.LocalDate
import java.util.*

data class MemberSignedUp(
    override val id: UUID,
    val name: String,
    val email: String,
    val passwordHash: String,
    val age: Int,
    val subscriptionDate: LocalDate,
    val validationExpirationDate: LocalDate,
    val activated: Boolean,
    ) : Event() {}