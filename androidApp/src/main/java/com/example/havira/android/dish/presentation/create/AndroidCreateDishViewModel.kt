package com.example.havira.android.dish.presentation.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.havira.dish.domain.interactors.DishInteractors
import com.example.havira.dish.presentation.create.CreateDishEvent
import com.example.havira.dish.presentation.create.CreateDishViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AndroidCreateDishViewModel @Inject constructor(
    private val dishInteractors: DishInteractors
) : ViewModel() {

    private val viewModel by lazy {
        CreateDishViewModel(
            dishInteractors = dishInteractors,
            coroutineScope = viewModelScope
        )
    }

    val state = viewModel.state

    fun onEvent(event: CreateDishEvent){
        viewModel.onEvent(event)
    }
}