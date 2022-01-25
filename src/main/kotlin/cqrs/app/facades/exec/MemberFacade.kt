package cqrs.app.facades.exec

import cqrs.app.facades.App.Companion.changePasswordHandler
import cqrs.app.facades.App.Companion.credentialDao
import cqrs.app.facades.App.Companion.profileDao
import cqrs.app.facades.App.Companion.signUpHandler
import cqrs.app.facades.App.Companion.validateEmailHandler
import cqrs.app.domain.command.ChangePassword
import cqrs.app.domain.command.SignUp
import cqrs.app.domain.command.ValidateEmail
import cqrs.app.domain.model.member.Password
import java.time.LocalDate
import java.util.*


typealias Json = Map<String, Any>

class MemberFacade {

    suspend fun signUp(json: Json): Json {
        val uuid = UUID.randomUUID()
        signUpHandler.handle(
            SignUp(
                id = uuid,
                date = LocalDate.now(),
                name = json["name"] as String,
                email = json["email"] as String,
                password = json["password"] as String,
                age = json["age"] as Int,
            )
        )
        val profile = profileDao.findById(uuid)
        return mapOf<String, Any>("profile" to profile)
    }

    suspend fun validateEmail(json: Json): Json {
        val uuid = json["id"] as UUID
        validateEmailHandler.handle(
            ValidateEmail(
                id = uuid,
                date = LocalDate.now(),
            )
        )
        val profile = profileDao.findById(uuid)
        return mapOf<String, Any>("profile" to profile)
    }

    suspend fun login(json: Json): Json {
        val username = json["login"] as String
        val password = Password(json["pwd"] as String)

        val credential = credentialDao.findByLoginAndPassword(username, password.hash)
            ?: return mapOf<String, Any>("error" to "Cannot find account for login/password")

        val profile = profileDao.findById(credential.id)
        return mapOf<String, Any>("profile" to profile)
    }

    suspend fun changePassword(json: Json): Json {
        val uuid = json["id"] as UUID
        changePasswordHandler.handle(
            ChangePassword(
                uuid,
                json["old"] as String,
                json["new"] as String,
            )
        )
        val profile = profileDao.findById(uuid)
        return mapOf<String, Any>("profile" to profile)
    }


}
