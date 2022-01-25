package cqrs.app.read_model.credentials

import cqrs.app.domain.infra.Handles
import cqrs.app.domain.model.member.event.MemberSignedUp

class MemberSignedUpHandler(private val credentialDao: CredentialDao) : Handles<MemberSignedUp> {

    override suspend fun handle(message: MemberSignedUp) {
        val credential = Credential(
            message.id,
            message.email,
            message.passwordHash
        )
        credentialDao.save(credential)
    }
}