package com.piotrkalin.havira.android.groupDish.presentation.create

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.piotrkalin.havira.dish.domain.interactors.DishInteractors
import com.piotrkalin.havira.dish.presentation.create.CreateDishEvent
import com.piotrkalin.havira.groupDish.presentation.CreateGroupDishViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AndroidCreateGroupDishViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val dishInteractors: DishInteractors
) : ViewModel() {

    private val groupId = savedStateHandle.get<String>("groupId")?.toLong() ?: -1L
    private val viewModel by lazy {
       CreateGroupDishViewModel(groupId, dishInteractors,viewModelScope)
    }

    val state = viewModel.state

    fun onEvent(event: CreateDishEvent) = viewModel.onEvent(event)
}