package cqrs.app.read_model.profile

import kotlinx.coroutines.delay
import java.util.*


data class Profile(
    val id: UUID,
    var email: String,
    var name: String,
    var age: Int,
    var activationAlertVisible: Boolean
)