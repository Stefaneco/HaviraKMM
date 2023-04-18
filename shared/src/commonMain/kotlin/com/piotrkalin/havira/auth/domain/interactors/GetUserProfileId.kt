package com.piotrkalin.havira.auth.domain.interactors

import com.piotrkalin.havira.auth.domain.ITokenDataSource

class GetUserProfileId(
    private val tokenSource: ITokenDataSource
) {
    operator fun invoke() : String? {
        val profileId = tokenSource.getProfileId()
        return profileId
    }
}