package com.example.havira.android.dish.presentation.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.havira.dish.domain.interactors.DishInteractors
import com.example.havira.dish.presentation.edit.DishEditEvent
import com.example.havira.dish.presentation.edit.DishEditViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AndroidDishEditViewModel @Inject constructor(
    private val dishInteractors: DishInteractors
): ViewModel() {

    private val viewModel by lazy {
        DishEditViewModel(
            dishInteractors,
            viewModelScope
        )
    }

    val state = viewModel.state

    fun onEvent(event: DishEditEvent){
        viewModel.onEvent(event)
    }

    fun loadDish(dishId: Long){
        viewModel.loadDish(dishId)
    }
}