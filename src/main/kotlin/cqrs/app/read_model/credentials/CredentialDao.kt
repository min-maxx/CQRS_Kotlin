package cqrs.app.read_model.credentials

import kotlinx.coroutines.delay
import java.util.*

class CredentialDao {
    private val credentials: MutableList<Credential> = mutableListOf()

    suspend fun save(credential: Credential) {
        credentials.add(credential)
        delay(200) //IO latency
    }

    suspend fun update(id: UUID, passwordHash: String) {
        val credential = credentials.single { it.id == id }
        delay(200) //IO latency
        credential.passwordHash = passwordHash
    }

    suspend fun emailExists(email: String): Boolean {
        val exists = credentials.any { it.login == email }
        delay(100) //IO latency
        return exists
    }

    suspend fun findByLoginAndPassword(username: String, passwordHash: String): Credential? {
        val credential = credentials.singleOrNull {
            it.login == username && it.passwordHash == passwordHash
        }
        delay(100) //IO latency
        return credential
    }
}