package com.piotrkalin.havira.auth.presentation

import com.piotrkalin.havira.auth.domain.interactors.AuthInteractors
import com.piotrkalin.havira.auth.domain.interactors.LoginWithGoogleState
import com.piotrkalin.havira.core.domain.util.toCommonStateFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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
                _state.update { it.copy(
                    stage = LoginStage.CONNECTING_TO_AZURE
                ) }
                viewModelScope.launch {
                    authInteractors.loginWithGoogle(event.idToken).collect { result ->
                        result.onSuccess { s ->
                            when(s){
                                LoginWithGoogleState.CONNECTED_TO_AZURE -> {
                                    _state.update { it.copy(
                                        stage = LoginStage.LOADING_PROFILE_INFO
                                    ) }
                                }
                                LoginWithGoogleState.PROFILE_FOUND -> {
                                    event.onSuccess()
                                }
                                LoginWithGoogleState.PROFILE_NOT_FOUND -> {
                                    _state.update { it.copy(
                                        stage = LoginStage.WAITING_FOR_PROFILE_CREATION
                                    ) }
                                }
                            }
                        }
                        result.onFailure { t ->
                            _state.update { it.copy(
                                stage = LoginStage.WAITING_FOR_PROVIDER_SELECTION,
                                error = t.message ?: "Unknown Error"
                            ) }
                        }
                    }
                }
            }
            LoginEvent.OnErrorSeen -> {
                _state.update { it.copy(
                    error = null
                ) }
            }
            is LoginEvent.OnImageSelected -> {
                _state.update { it.copy(
                    image = event.image
                ) }
            }
            is LoginEvent.EditName -> {
                _state.update { it.copy(
                    name = event.name,
                    isValidProfile = isValidProfile(event.name)
                ) }
            }
            is LoginEvent.SaveProfile -> {
                _state.update { it.copy(
                    stage = LoginStage.CREATING_PROFILE
                ) }
                viewModelScope.launch {
                    val result = authInteractors.createUserProfile(
                        name = _state.value.name.trim(),
                        image = _state.value.image
                    )
                    result.onSuccess { _ ->
                        event.onSuccess()
                    }
                    result.onFailure { t ->
                        _state.update { it.copy(
                            error = t.message,
                            stage = LoginStage.WAITING_FOR_PROFILE_CREATION
                        ) }
                    }
                }
            }
            is LoginEvent.SetError -> {
                _state.update { it.copy(
                    error = event.errorMessage
                ) }
            }
        }
    }

    private fun isValidProfile(name : String) : Boolean{
        return name.trim().length > 1
    }
}