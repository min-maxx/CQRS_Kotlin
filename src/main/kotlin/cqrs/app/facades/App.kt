package cqrs.app.facades

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import cqrs.app.domain.command.ChangePassword
import cqrs.app.domain.command.ChangePasswordHandler
import cqrs.app.domain.command.SignUp
import cqrs.app.domain.command.SignUpHandler
import cqrs.app.domain.command.ValidateEmail
import cqrs.app.domain.command.ValidateEmailHandler
import cqrs.app.domain.command.cross.WithAuthorization
import cqrs.app.domain.command.cross.WithLogging
import cqrs.app.domain.infra.Handles
import cqrs.app.domain.infra.bus.EventBus
import cqrs.app.domain.infra.repositories.EventStoreRepository
import cqrs.app.domain.infra.repositories.InMemoryEventStore
import cqrs.app.domain.infra.services.CredentialsAuthenticationService
import cqrs.app.domain.infra.services.MemberSignedUpHandler
import cqrs.app.domain.infra.services.MemberValidationExtendedHandler
import cqrs.app.domain.model.member.Member
import cqrs.app.read_model.credentials.CredentialDao
import cqrs.app.read_model.credentials.PasswordChangedHandler
import cqrs.app.read_model.profile.MemberEmailValidatedHandler
import cqrs.app.read_model.profile.ProfileDao
import java.util.*
import cqrs.app.read_model.credentials.MemberSignedUpHandler as CredentialsMemberSignedUpHandler
import cqrs.app.read_model.profile.MemberSignedUpHandler as ProfileMemberSignedUpHandler


class App {
    init {
        Locale.setDefault(Locale.ENGLISH)
    }

    companion object {
        //setup : dependencies injection
        //1.read Daos
        val credentialDao = CredentialDao()
        val profileDao = ProfileDao()

        val credentialsMemberSignedUpHandler = WithLogging(CredentialsMemberSignedUpHandler(credentialDao))
        val passwordChangedHandler = WithLogging(PasswordChangedHandler(credentialDao))
        val profileMemberSignedUpHandler = WithLogging(ProfileMemberSignedUpHandler(profileDao))
        val memberSignedUpHandler = WithLogging(MemberSignedUpHandler())
        val memberValidationExtendedHandler = WithLogging(MemberValidationExtendedHandler())
        val memberEmailValidatedHandler = WithLogging(MemberEmailValidatedHandler(profileDao))

        //2.Bus
        private val eventBus = EventBus().apply {
            register(credentialsMemberSignedUpHandler)
            register(passwordChangedHandler)
            register(profileMemberSignedUpHandler)
            register(memberSignedUpHandler)
            register(memberValidationExtendedHandler)
            register(memberEmailValidatedHandler)
        }

        //3.Store
        private val jsonMapper = jacksonObjectMapper().registerModule(JavaTimeModule())
        private val eventStore = InMemoryEventStore(eventBus, jsonMapper)
        private val memberRepository = EventStoreRepository<Member>(eventStore)

        //4.commandHandlers
        private val authenticationService = CredentialsAuthenticationService(credentialDao)
        val signUpHandler: Handles<SignUp> = WithLogging(SignUpHandler(memberRepository, authenticationService))
        val validateEmailHandler: Handles<ValidateEmail> = WithLogging(ValidateEmailHandler(memberRepository))
        val changePasswordHandler: Handles<ChangePassword> =
            WithLogging(WithAuthorization(ChangePasswordHandler(memberRepository)))
    }


}