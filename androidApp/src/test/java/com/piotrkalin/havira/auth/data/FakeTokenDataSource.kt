package com.piotrkalin.havira.auth.data

import com.piotrkalin.havira.auth.domain.ITokenDataSource

class FakeTokenDataSource : ITokenDataSource {

    private val fakeAuthToken: MutableMap<String, String> = mutableMapOf()

    override fun getAuthToken(): String? {
        return fakeAuthToken["authToken"]
    }

    override fun updateAuthToken(token: String) {
        fakeAuthToken["authToken"] = token
    }

    override fun getProfileId(): String? {
        return fakeAuthToken["userId"]
    }

    override fun updateProfileId(userId: String) {
        fakeAuthToken["userId"] = userId
    }

    override fun removeAllTokens() {
        fakeAuthToken.remove("authToken")
    }
}
