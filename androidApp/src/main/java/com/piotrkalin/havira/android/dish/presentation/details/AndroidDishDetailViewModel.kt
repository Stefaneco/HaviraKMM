package com.piotrkalin.havira.android.dish.presentation.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.piotrkalin.havira.dish.domain.interactors.DishInteractors
import com.piotrkalin.havira.dish.presentation.detail.DishDetailEvent
import com.piotrkalin.havira.dish.presentation.detail.DishDetailViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AndroidDishDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val dishInteractors: DishInteractors
): ViewModel() {

    private val dishId = savedStateHandle.get<String>("dishId")?.toLong() ?: -1L
    private val viewModel by lazy {
        DishDetailViewModel(
            dishId,
            dishInteractors,
            viewModelScope
        )
    }

    val state = viewModel.state

    fun onEvent(event: DishDetailEvent){
        viewModel.onEvent(event)
    }
}