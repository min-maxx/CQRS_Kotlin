package cqrs.app.domain.infra.services

import cqrs.app.domain.infra.Handles
import cqrs.app.domain.model.member.event.MemberSignedUp
import kotlinx.coroutines.delay


class MemberSignedUpHandler : Handles<MemberSignedUp> {
    override suspend fun handle(message: MemberSignedUp) {
        //do stuff
        delay(300) //IO latency
    }
}



