package com.example.havira.android.dish.list.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.havira.dish.interactors.DishInteractors
import com.example.havira.dish.presentation.list.DishListEvent
import com.example.havira.dish.presentation.list.DishListViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AndroidDishListViewModel @Inject constructor(
    private val dishInteractors: DishInteractors
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