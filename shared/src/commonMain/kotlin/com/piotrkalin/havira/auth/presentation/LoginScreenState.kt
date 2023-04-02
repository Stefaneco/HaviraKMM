package com.piotrkalin.havira.auth.presentation

import com.piotrkalin.havira.core.domain.util.ImageFile

data class LoginScreenState(
    val error : String? = null,
    val stage : LoginStage = LoginStage.WAITING_FOR_PROVIDER_SELECTION,
    val name : String = "",
    val image : ImageFile? = null,
    val isValidProfile : Boolean = false
)

enum class LoginStage {
    WAITING_FOR_PROVIDER_SELECTION,
    CONNECTING_TO_AZURE,
    LOADING_PROFILE_INFO,
    WAITING_FOR_PROFILE_CREATION,
    CREATING_PROFILE
}