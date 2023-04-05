package com.piotrkalin.havira.auth.domain.interactors

import com.piotrkalin.havira.auth.domain.IProfileService
import com.piotrkalin.havira.auth.domain.ITokenDataSource
import com.piotrkalin.havira.core.domain.util.IImageFile

class CreateUserProfile(
    private val profileService: IProfileService,
    private val tokenDataSource: ITokenDataSource
) {
    suspend operator fun invoke(name: String, image : IImageFile?) : Result<Unit?> {
        return try {
            println("Auth CreateUserProfile: Started")
            val byteArray = image?.toResizedByteArray(300, 300)
            //val byteArray = image?.toByteArray()
            println("Auth CreateUserProfile: ByteArray created")
            val createdProfile = profileService.createUserProfile(name, byteArray)
            println("Auth CreateUserProfile: Profile Created")
            tokenDataSource.updateProfileId(createdProfile.id)
            println("Auth CreateUserProfile: Profile token updated")
            Result.success(null)
        } catch (e: Exception){
            println("Auth CreateUserProfile error: ${e.message}")
            Result.failure(e)
        }
    }


}