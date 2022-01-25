package cqrs.app.read_model.profile

import cqrs.app.domain.infra.Handles
import cqrs.app.domain.model.member.event.MemberSignedUp

class MemberSignedUpHandler(private val profileDao: ProfileDao) : Handles<MemberSignedUp> {
    override suspend fun handle(message: MemberSignedUp) {
        val profile = Profile(
            message.id,
            message.email,
            message.name,
            message.age,
            !message.activated
        )
        profileDao.save(profile)
    }
}