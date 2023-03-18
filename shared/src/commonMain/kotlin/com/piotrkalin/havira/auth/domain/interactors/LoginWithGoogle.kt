package com.piotrkalin.havira.auth.domain.interactors

import com.piotrkalin.havira.auth.domain.IAuthService
import com.piotrkalin.havira.auth.domain.ITokenDataSource

class LoginWithGoogle(
    private val authService: IAuthService,
    private val tokenDataSource: ITokenDataSource
) {
    suspend operator fun invoke(idToken: String){
        println("Auth LoginWithGoogle: Interactor started")
        val token = authService.getAuthTokenWithGoogle(idToken)
        println("Auth LoginWithGoogle: Azure token received")
        tokenDataSource.updateAuthToken(token)
        println("Auth LoginWithGoogle: Azure token saved on device")
    }
}