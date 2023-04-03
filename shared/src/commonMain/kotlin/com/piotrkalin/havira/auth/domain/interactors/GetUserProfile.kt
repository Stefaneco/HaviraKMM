package com.piotrkalin.havira.auth.domain.interactors

import com.piotrkalin.havira.auth.domain.IProfileService
import com.piotrkalin.havira.auth.domain.model.UserProfile
import com.piotrkalin.havira.core.domain.util.CommonFlow
import com.piotrkalin.havira.core.domain.util.toCommonFlow
import kotlinx.coroutines.flow.flow

class GetUserProfile(
    private val profileService: IProfileService
) {
    operator fun invoke() : CommonFlow<Result<UserProfile>> = flow {
        try {
            val response = profileService.getUserProfile()
            emit(Result.success(response))
        }catch (e: Exception){
            emit(Result.failure(e))
        }
    }.toCommonFlow()
}