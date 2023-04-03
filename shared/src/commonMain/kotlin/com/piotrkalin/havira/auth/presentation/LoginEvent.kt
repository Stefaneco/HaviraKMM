package com.piotrkalin.havira.auth.presentation

import com.piotrkalin.havira.core.domain.util.ImageFile

sealed class LoginEvent {
    class GoogleLogin(val idToken : String, val onSuccess: () -> Unit = {}) : LoginEvent()
    object OnErrorSeen : LoginEvent()
    data class OnImageSelected(val image : ImageFile) : LoginEvent()
    data class EditName(val name: String) : LoginEvent()
    class SaveProfile(val onSuccess: () -> Unit = {}) : LoginEvent()
}