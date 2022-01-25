package cqrs.app.domain.command

import cqrs.app.domain.infra.Command
import cqrs.app.domain.model.member.MemberRepository
import cqrs.app.domain.model.member.Password
import cqrs.app.domain.infra.Handles
import cqrs.app.domain.infra.Message
import java.util.*

class ChangePasswordHandler(private val repository: MemberRepository) : Handles<ChangePassword> {
    override suspend fun handle(message: ChangePassword) {
        val member = repository.getBy(message.id)
        member.changePassword(Password(message.old), Password(message.new))
        repository.save(member)
    }
}

data class ChangePassword(
    override val id: UUID,
    val old: String,
    val new: String,
) : Command() {}
