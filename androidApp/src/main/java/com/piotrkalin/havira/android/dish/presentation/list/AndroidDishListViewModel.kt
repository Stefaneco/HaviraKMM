package com.piotrkalin.havira.android.dish.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.piotrkalin.havira.dish.presentation.list.DishListEvent
import com.piotrkalin.havira.dish.presentation.list.DishListViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AndroidDishListViewModel @Inject constructor(
    private val dishInteractors: com.piotrkalin.havira.dish.domain.interactors.DishInteractors
): ViewModel() {

    private val viewModel by lazy {
        DishListViewModel(
            dishInteractors = dishInteractors,
            coroutineScope = viewModelScope
        )
    }

    val state = viewModel.state

    fun onEvent(event: DishListEvent){
        viewModel.onEvent(event)
    }
}