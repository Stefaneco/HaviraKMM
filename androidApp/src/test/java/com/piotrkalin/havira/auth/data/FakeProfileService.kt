package com.piotrkalin.havira.auth.data

import com.piotrkalin.havira.auth.domain.IProfileService
import com.piotrkalin.havira.auth.domain.model.UserProfile
import com.piotrkalin.havira.core.data.remote.NotFoundException
import kotlinx.coroutines.delay

class FakeProfileService : IProfileService {

    private val fakeUserProfiles = mutableMapOf<String, UserProfile>()
    var fakeUserId = "fake_user_id"
    var throwsError = false
    var errorMessage = "Profile service error"


    override suspend fun getUserProfile(): UserProfile {
        delay(500) // Simulate network delay
        if(throwsError) throw RuntimeException(errorMessage)
        return fakeUserProfiles[fakeUserId] ?: throw NotFoundException()
    }

    override suspend fun getUserProfileOrNull(): UserProfile? {
        delay(500) // Simulate network delay
        if(throwsError) throw RuntimeException(errorMessage)
        return fakeUserProfiles[fakeUserId]
    }

    override suspend fun createUserProfile(name: String, image: ByteArray?): UserProfile {
        delay(500) // Simulate network delay
        if(throwsError) throw RuntimeException(errorMessage)
        val userProfile = UserProfile(fakeUserId, name)
        fakeUserProfiles[fakeUserId] = userProfile
        return userProfile
    }

    override suspend fun uploadPhoto(image: ByteArray): String {
        delay(500) // Simulate network delay
        if(throwsError) throw RuntimeException(errorMessage)
        return "fake_image_url"
    }
}
