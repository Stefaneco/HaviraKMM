package com.piotrkalin.havira.android.group.join

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.piotrkalin.havira.group.domain.interactors.GroupInteractors
import com.piotrkalin.havira.group.presentation.join.JoinGroupEvent
import com.piotrkalin.havira.group.presentation.join.JoinGroupViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AndroidJoinGroupViewModel  @Inject constructor(
    private val groupInteractors: GroupInteractors
) : ViewModel() {

    private val viewModel by lazy {
        JoinGroupViewModel(
            groupInteractors = groupInteractors,
            coroutineScope = viewModelScope
        )
    }

    val state = viewModel.state

    fun onEvent(event: JoinGroupEvent){
        viewModel.onEvent(event)
    }
}