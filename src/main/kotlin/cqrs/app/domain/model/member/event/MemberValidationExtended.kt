package cqrs.app.domain.model.member.event

import cqrs.app.domain.infra.Event
import java.time.LocalDate
import java.util.*

data class MemberValidationExtended(
    override val id: UUID,
    val newValidationExpirationDate: LocalDate
) : Event() {}