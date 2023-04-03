package com.piotrkalin.havira.auth.domain

interface ITokenDataSource {

    fun getAuthToken() : String?

    fun updateAuthToken(token: String)

    fun getProfileId() : String?

    fun updateProfileId(userId: String)

    fun removeAllTokens()
}