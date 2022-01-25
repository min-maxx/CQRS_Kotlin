package cqrs.app.domain.command

import cqrs.app.domain.infra.Command
import cqrs.app.domain.model.member.MemberRepository
import cqrs.app.domain.infra.Handles
import cqrs.app.domain.infra.Message
import java.time.LocalDate
import java.util.*

class ValidateEmailHandler(private val repository: MemberRepository) : Handles<ValidateEmail> {
    override suspend fun handle(message: ValidateEmail) {
        val member = repository.getBy(message.id)
        member.validate(message.date)
        repository.save(member)
    }
}

data class ValidateEmail(
    override val id: UUID,
    val date: LocalDate,
) : Command() {}