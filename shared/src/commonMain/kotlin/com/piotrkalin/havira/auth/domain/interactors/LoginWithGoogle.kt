package com.piotrkalin.havira.auth.domain.interactors

import com.piotrkalin.havira.auth.domain.IAuthService
import com.piotrkalin.havira.auth.domain.ITokenDataSource
import com.piotrkalin.havira.core.domain.util.CommonFlow
import com.piotrkalin.havira.core.domain.util.toCommonFlow
import kotlinx.coroutines.flow.flow

class LoginWithGoogle(
    private val authService: IAuthService,
    private val tokenDataSource: ITokenDataSource
) {
    suspend operator fun invoke(idToken: String) : CommonFlow<Result<Nothing?>> = flow {
        try{
            println("Auth LoginWithGoogle: Interactor started")
            val token = authService.getAuthTokenWithGoogle(idToken)
            println("Auth LoginWithGoogle: Azure token received")
            tokenDataSource.updateAuthToken(token)
            println("Auth LoginWithGoogle: Azure token saved on device")
            emit(Result.success(null))
        } catch (e: Exception){
            emit(Result.failure(e))
        }
    }.toCommonFlow()
}