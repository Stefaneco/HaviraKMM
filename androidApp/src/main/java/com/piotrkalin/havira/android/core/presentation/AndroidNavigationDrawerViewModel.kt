package com.piotrkalin.havira.android.core.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.piotrkalin.havira.auth.domain.interactors.AuthInteractors
import com.piotrkalin.havira.core.presentation.NavigationDrawerEvent
import com.piotrkalin.havira.core.presentation.NavigationDrawerViewModel
import com.piotrkalin.havira.group.domain.interactors.GroupInteractors
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AndroidNavigationDrawerViewModel @Inject constructor(
    private val groupInteractors: GroupInteractors,
    private val authInteractors: AuthInteractors,
) : ViewModel() {

    private val viewModel by lazy {
        NavigationDrawerViewModel(
            groupInteractors,
            authInteractors,
            viewModelScope
        )
    }

    val state = viewModel.state

    fun onEvent(event: NavigationDrawerEvent) = viewModel.onEvent(event)
}