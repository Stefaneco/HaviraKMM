package com.piotrkalin.havira.android.group.create.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.piotrkalin.havira.group.presentation.create.CreateGroupEvent
import com.piotrkalin.havira.group.presentation.create.CreateGroupViewModel
import com.piotrkalin.havira.group.domain.interactors.GroupInteractors
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AndroidCreateGroupViewModel @Inject constructor(
    private val groupInteractors: GroupInteractors
) : ViewModel() {

    private val viewModel by lazy {
        CreateGroupViewModel(
            groupInteractors = groupInteractors,
            coroutineScope = viewModelScope
        )
    }

    val state = viewModel.state

    fun onEvent(event: CreateGroupEvent){
        viewModel.onEvent(event)
    }
}