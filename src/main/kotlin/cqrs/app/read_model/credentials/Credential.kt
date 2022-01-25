package cqrs.app.read_model.credentials

import java.util.*

data class Credential(
    val id: UUID,
    val login: String,
    var passwordHash: String
)