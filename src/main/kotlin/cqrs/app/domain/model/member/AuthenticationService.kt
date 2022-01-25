package cqrs.app.domain.model.member

interface AuthenticationService {
    suspend fun checkExist(email: String): Boolean
}