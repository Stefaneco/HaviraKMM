package com.piotrkalin.havira.auth.domain

interface IAuthService {

    suspend fun getAuthTokenWithGoogle(idToken: String) : String


}