package cqrs.app.domain.command.cross

import cqrs.app.domain.infra.Handles
import cqrs.app.domain.infra.Message

class WithAuthorization<T : Message>(val next: Handles<T>) : Handles<T> {

    override suspend fun handle(message: T) {
//        var isOK: Boolean = false
        var isOK: Boolean = true
        //isOk =accountService.check(...)
        if (isOK) {
            next.handle(message)
        } else {
            throw IllegalAccessException("user not authorized")
        }
    }

}