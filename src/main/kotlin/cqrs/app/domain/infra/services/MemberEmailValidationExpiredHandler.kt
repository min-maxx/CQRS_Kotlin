package cqrs.app.domain.infra.services

import cqrs.app.domain.infra.Handles
import cqrs.app.domain.model.member.event.MemberValidationExtended


class MemberValidationExtendedHandler : Handles<MemberValidationExtended> {
    override suspend fun handle(message: MemberValidationExtended) {
        //do stuff
    }
}



