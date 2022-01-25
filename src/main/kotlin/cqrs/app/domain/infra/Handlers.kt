package cqrs.app.domain.infra

import java.util.*

abstract class Message{
    abstract val id: UUID
}

abstract class Command : Message()
abstract class Event : Message()


interface Handles<T : Message> {
    suspend fun handle(message: T)
}



