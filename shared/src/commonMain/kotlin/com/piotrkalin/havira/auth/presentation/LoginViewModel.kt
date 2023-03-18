package com.piotrkalin.havira.auth.presentation

import com.piotrkalin.havira.auth.domain.interactors.AuthInteractors
import com.piotrkalin.havira.core.domain.util.toCommonStateFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authInteractors: AuthInteractors,
    private val coroutineScope: CoroutineScope?
) {
    private val viewModelScope = coroutineScope ?: CoroutineScope(Dispatchers.Main)

    private val _state = MutableStateFlow(LoginScreenState())
    val state = _state.asStateFlow().toCommonStateFlow()

    fun onEvent(event: LoginEvent){
        when(event){
            is LoginEvent.GoogleLogin -> {
                viewModelScope.launch {
                    authInteractors.loginWithGoogle(event.idToken)
                    event.onSuccess()
                }
            }
        }
    }
}