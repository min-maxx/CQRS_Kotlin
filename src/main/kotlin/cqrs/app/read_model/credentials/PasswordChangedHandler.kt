package cqrs.app.read_model.credentials

import cqrs.app.domain.infra.Handles
import cqrs.app.domain.model.member.event.PasswordChanged

class PasswordChangedHandler(private val credentialDao: CredentialDao) : Handles<PasswordChanged> {
    override suspend fun handle(message: PasswordChanged) {
        credentialDao.update(message.id, message.passwordHash)
    }
}