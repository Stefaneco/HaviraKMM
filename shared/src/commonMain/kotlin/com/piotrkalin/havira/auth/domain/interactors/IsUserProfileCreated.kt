package com.piotrkalin.havira.auth.domain.interactors

import com.piotrkalin.havira.auth.domain.IProfileService
import com.piotrkalin.havira.auth.domain.ITokenDataSource

class IsUserProfileCreated(
    private val profileService: IProfileService,
    private val tokenDataSource: ITokenDataSource
) {
    suspend operator fun invoke() : Result<Boolean> {
        try {
            val onDeviceProfile = tokenDataSource.getProfileId()
            if (onDeviceProfile != null) return Result.success(true)
            val remoteProfile = profileService.getUserProfileOrNull()
            if (remoteProfile != null) return Result.success(true)
            return Result.success(false)

        } catch (e: Exception){
            return Result.failure(e)
        }
    }
}