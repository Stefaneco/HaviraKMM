package com.piotrkalin.havira.auth.domain

import com.piotrkalin.havira.auth.domain.model.UserProfile

interface IProfileService {

    suspend fun getUserProfile() : UserProfile

    suspend fun getUserProfileOrNull() : UserProfile?

    suspend fun createUserProfile(name : String, image : ByteArray?) : UserProfile

    suspend fun uploadPhoto(image : ByteArray) : String
}