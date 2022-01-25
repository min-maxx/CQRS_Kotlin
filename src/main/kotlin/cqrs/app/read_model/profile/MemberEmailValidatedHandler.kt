package cqrs.app.read_model.profile

import cqrs.app.domain.infra.Handles
import cqrs.app.domain.model.member.event.MemberEmailValidated

class MemberEmailValidatedHandler(
    private val profileDao: ProfileDao
) : Handles<MemberEmailValidated> {

    override suspend fun handle(message: MemberEmailValidated) {
        profileDao.update(message.id, false)
    }

}