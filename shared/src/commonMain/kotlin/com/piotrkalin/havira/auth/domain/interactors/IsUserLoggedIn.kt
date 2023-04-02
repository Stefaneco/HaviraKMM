package com.piotrkalin.havira.auth.domain.interactors

import com.piotrkalin.havira.auth.domain.ITokenDataSource

class IsUserLoggedIn(
    private val tokenSource: ITokenDataSource
) {
    operator fun invoke() : Boolean {
        val profileId = tokenSource.getProfileId()
        return profileId != null
    }
}