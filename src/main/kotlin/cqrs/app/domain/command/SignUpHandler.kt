package cqrs.app.domain.command

import cqrs.app.domain.infra.Command
import cqrs.app.domain.infra.Handles
import cqrs.app.domain.model.member.Age
import cqrs.app.domain.model.member.AuthenticationService
import cqrs.app.domain.model.member.Email
import cqrs.app.domain.model.member.Member
import cqrs.app.domain.model.member.MemberRepository
import cqrs.app.domain.model.member.Password
import java.time.LocalDate
import java.util.*

class SignUpHandler(
    private val repository: MemberRepository,
    private val authenticationService: AuthenticationService
) : Handles<SignUp> {

    override suspend fun handle(message: SignUp) {
        if (authenticationService.checkExist(message.email))
            throw IllegalStateException("Email already exists. ...")
        val member = Member(
            message.id,
            Email(message.email),
            Password(message.password),
            message.name,
            Age(message.age),
            message.date
        )
        repository.save(member)
    }
}

data class SignUp(
    override val id: UUID,
    val name: String,
    val email: String,
    val password: String,
    val age: Int,
    val date: LocalDate
) : Command() {}