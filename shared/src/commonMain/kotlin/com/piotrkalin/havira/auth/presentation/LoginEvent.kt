package com.piotrkalin.havira.auth.presentation

sealed class LoginEvent {
    class GoogleLogin(val idToken : String, val onSuccess: () -> Unit = {}) : LoginEvent()
}