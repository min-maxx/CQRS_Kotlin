package cqrs.app.domain.model.member.event

import cqrs.app.domain.infra.Event
import java.util.*

data class PasswordChanged(
    override val id: UUID,
    val passwordHash: String
) : Event() {}