package com.piotrkalin.havira.auth.domain

interface ITokenDataSource {

    fun getAuthToken() : String?

    fun updateAuthToken(token: String)
}