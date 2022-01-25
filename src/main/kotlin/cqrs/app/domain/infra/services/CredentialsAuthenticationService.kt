package cqrs.app.domain.infra.services

import cqrs.app.domain.model.member.AuthenticationService
import cqrs.app.read_model.credentials.CredentialDao

class CredentialsAuthenticationService(
    private val credentialDao: CredentialDao

) : AuthenticationService {
    override suspend fun checkExist(email: String): Boolean {
       return credentialDao.emailExists(email)
    }
}