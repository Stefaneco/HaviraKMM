package com.piotrkalin.havira.android.groupDish.presentation.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.piotrkalin.havira.dish.domain.interactors.DishInteractors
import com.piotrkalin.havira.dish.presentation.edit.DishEditEvent
import com.piotrkalin.havira.groupDish.presentation.EditGroupDishViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AndroidEditGroupDishViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val dishInteractors: DishInteractors
) : ViewModel() {

    private val dishId = savedStateHandle.get<String>("dishId")?.toLong() ?: -1L
    private val viewModel by lazy {
        EditGroupDishViewModel(dishId, dishInteractors, viewModelScope)
    }

    val state = viewModel.state

    fun onEvent(event: DishEditEvent) = viewModel.onEvent(event)
}