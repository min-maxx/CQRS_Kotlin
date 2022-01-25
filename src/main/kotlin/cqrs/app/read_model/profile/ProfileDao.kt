package cqrs.app.read_model.profile

import kotlinx.coroutines.delay
import java.util.*

class ProfileDao {
    private val profiles: MutableList<Profile> = mutableListOf()

    suspend fun save(profile: Profile) {
        profiles.add(profile)
        delay(200) //IO latency
    }

    suspend fun update(id: UUID, activationAlertVisible: Boolean) {
        val profile = profiles.single { it.id == id }
        delay(200) //IO latency
        profile.activationAlertVisible = activationAlertVisible
    }

    suspend fun findById(uuid: UUID): Profile {
        val single = profiles.single { profile -> profile.id == uuid }
        delay(100) //IO latency
        return single
    }
}