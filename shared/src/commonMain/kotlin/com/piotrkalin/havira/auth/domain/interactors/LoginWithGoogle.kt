package com.piotrkalin.havira.auth.domain.interactors

import com.piotrkalin.havira.auth.domain.IAuthService
import com.piotrkalin.havira.auth.domain.IProfileService
import com.piotrkalin.havira.auth.domain.ITokenDataSource
import com.piotrkalin.havira.core.data.remote.TimeoutException
import com.piotrkalin.havira.core.domain.util.CommonFlow
import com.piotrkalin.havira.core.domain.util.toCommonFlow
import io.ktor.util.network.*
import kotlinx.coroutines.flow.flow

class LoginWithGoogle(
    private val authService: IAuthService,
    private val profileService: IProfileService,
    private val tokenDataSource: ITokenDataSource
) {
    suspend operator fun invoke(idToken: String) : CommonFlow<Result<LoginWithGoogleState>> = flow {
        try{
            println("Auth LoginWithGoogle: Interactor started")
            val token = authService.getAuthTokenWithGoogle(idToken)
            println("Auth LoginWithGoogle: Azure token received")
            tokenDataSource.updateAuthToken(token)
            println("Auth LoginWithGoogle: Azure token saved on device")
            emit(Result.success(LoginWithGoogleState.CONNECTED_TO_AZURE))

            val onDeviceProfile = tokenDataSource.getProfileId()
            if (onDeviceProfile != null) emit(Result.success(LoginWithGoogleState.PROFILE_FOUND))
            val remoteProfile = profileService.getUserProfileOrNull()
            if (remoteProfile != null) {
                tokenDataSource.updateProfileId(remoteProfile.id)
                emit(Result.success(LoginWithGoogleState.PROFILE_FOUND))
            }
            emit(Result.success(LoginWithGoogleState.PROFILE_NOT_FOUND))
        }
        catch (e: UnresolvedAddressException) {
            emit(Result.failure(TimeoutException(message = "No internet connection")))
        }
        catch (e: Exception){
            emit(Result.failure(e))
        }
    }.toCommonFlow()
}

enum class LoginWithGoogleState {
    CONNECTED_TO_AZURE,
    PROFILE_FOUND,
    PROFILE_NOT_FOUND
}