package cqrs.app.domain.model.member.event

import cqrs.app.domain.infra.Event
import java.util.*

data class MemberEmailValidated(override val id: UUID) : Event() {}