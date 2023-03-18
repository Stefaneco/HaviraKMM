package com.piotrkalin.havira.auth.domain.interactors

import com.piotrkalin.havira.auth.domain.IAuthService
import com.piotrkalin.havira.auth.domain.ITokenDataSource
import com.piotrkalin.havira.core.domain.util.CommonFlow
import com.piotrkalin.havira.core.domain.util.toCommonFlow
import kotlinx.coroutines.flow.flow

class Logout(
    private val authService: IAuthService,
    private val tokenDataSource: ITokenDataSource
) {
    suspend operator fun invoke() : CommonFlow<Result<Nothing?>> = flow {
        try {
            println("Auth Logout: Interactor started")
            authService.logout()
            println("Auth Logout: Logged out of Azure")
            tokenDataSource.removeAllTokens()
            println("Auth Logout: Removed all tokens form local storage")
            emit(Result.success(null))
        } catch (e: Exception){
            emit(Result.failure(e))
        }
    }.toCommonFlow()
}