package cqrs.client

import cqrs.app.facades.App
import cqrs.app.facades.exec.MemberFacade
import cqrs.app.read_model.profile.Profile
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


fun main() {
    App() //Launch app for setup

    runBlocking {
        repeat(10) {
            launch {
                //1. Signup
                val firstname = randomName()
                val lastname = randomName()
                val signUpResponse = MemberFacade().signUp(
                    json = mapOf(
                        "age" to randomAge(),
                        "email" to toEmail(firstname, lastname),
                        "name" to "$firstname $lastname",
                        "password" to "12345678",
                    )
                )
                println("signUpResponse = $signUpResponse")
                //2. Valider son email
                val profile: Profile = signUpResponse["profile"] as Profile
                val validateEmailResponse = MemberFacade().validateEmail(
                    json = mapOf(
                        "id" to profile.id,
                        "email" to profile.email,
                    )
                )
                println("validateEmailResponse = $validateEmailResponse")
                val loginResponseKO = MemberFacade().login(
                    json = mapOf(
                        "login" to profile.email,
                        "pwd" to "wrong_pwd",
                    )
                )
                println("loginResponseKO = $loginResponseKO")

                val loginResponseOK = MemberFacade().login(
                    json = mapOf(
                        "login" to profile.email,
                        "pwd" to "12345678",
                    )
                )
                println("loginResponseOK = $loginResponseOK")

                //3. Modifier son mdp
               val changePasswordResponse = MemberFacade().changePassword(
                    json = mapOf(
                        "id" to profile.id,
                        "old" to "12345678",
                        "new" to "abcdefgh",
                    )
                )
                println("changePasswordResponse = $changePasswordResponse")
            }
        }
    }
}

private fun randomName() =
    ('A'..'Z').random() + List((3..8).shuffled().first()) { ('a'..'z').random() }.joinToString("")

private fun randomAge() = (18..80).shuffled().first()


private fun toEmail(firstname: String, lastname: String) =
    "$firstname.$lastname@fakemail.com".lowercase()
