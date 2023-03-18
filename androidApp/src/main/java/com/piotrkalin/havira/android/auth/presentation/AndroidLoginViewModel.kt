package com.piotrkalin.havira.android.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.piotrkalin.havira.auth.domain.interactors.AuthInteractors
import com.piotrkalin.havira.auth.presentation.LoginEvent
import com.piotrkalin.havira.auth.presentation.LoginViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AndroidLoginViewModel @Inject constructor(
    private val authInteractors: AuthInteractors
) : ViewModel() {

    private val viewModel by lazy {
        LoginViewModel(authInteractors, viewModelScope)
    }

    val state = viewModel.state

    fun onEvent(event: LoginEvent){
        viewModel.onEvent(event)
    }
}