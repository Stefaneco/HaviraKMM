package com.piotrkalin.havira.auth.data

import com.piotrkalin.havira.auth.domain.IProfileService
import com.piotrkalin.havira.auth.domain.model.UserProfile
import com.piotrkalin.havira.core.data.remote.NotFoundException
import kotlinx.coroutines.delay

class FakeProfileService : IProfileService {

    private val fakeUserProfiles = mutableMapOf<String, UserProfile>()
    private var fakeUserId = "fake_user_id"

    public fun setFakeUserId(id: String) {
        fakeUserId = id
    }

    override suspend fun getUserProfile(): UserProfile {
        delay(500) // Simulate network delay
        return fakeUserProfiles[fakeUserId] ?: throw NotFoundException()
    }

    override suspend fun getUserProfileOrNull(): UserProfile? {
        delay(500) // Simulate network delay
        return fakeUserProfiles[fakeUserId]
    }

    override suspend fun createUserProfile(name: String, image: ByteArray?): UserProfile {
        delay(500) // Simulate network delay
        val userProfile = UserProfile(fakeUserId, name)
        fakeUserProfiles[fakeUserId] = userProfile
        return userProfile
    }

    override suspend fun uploadPhoto(image: ByteArray): String {
        delay(500) // Simulate network delay
        return "fake_image_url"
    }
}
