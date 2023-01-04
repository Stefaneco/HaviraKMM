package com.example.havira.dish.presentation.detail

import com.example.havira.core.domain.util.Resource
import com.example.havira.core.domain.util.toCommonStateFlow
import com.example.havira.dish.interactors.DishInteractors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DishDetailViewModel(
    private val dishInteractors: DishInteractors,
    private val coroutineScope: CoroutineScope?
) {

    private val viewModelScope = coroutineScope ?: CoroutineScope(Dispatchers.Main)

    private val _state = MutableStateFlow(DishDetailState())
    val state = _state.asStateFlow().toCommonStateFlow()

    fun onEvent(event: DishDetailEvent){
        when(event){
            is DishDetailEvent.OpenDishPrepCreator -> {
                _state.update { it.copy(
                    isDishPrepCreatorOpen = true
                )  }
            }
            is DishDetailEvent.CloseDishPrepCreator -> {
                _state.update { it.copy(
                    isDishPrepCreatorOpen = false
                )  }
            }
            is DishDetailEvent.AddDishPrep -> {
                _state.update { it.copy(
                    isDishPrepCreatorOpen = false
                )  }
            }
            is DishDetailEvent.Edit -> {

            }
        }
    }

    fun loadDish(dishId: Long){
        if (_state.value.dish != null) return
        viewModelScope.launch {
            dishInteractors.getDishById(dishId).collect { dishResource ->
                when(dishResource) {
                    is Resource.Success -> {
                        _state.update { it.copy(
                            dish = dishResource.data,
                            isLoading = false
                        ) }
                    }
                    is Resource.Error -> {
                        _state.update { it.copy(
                            error = dishResource.throwable?.message ?: "Unknown error",
                            isLoading = false
                        ) }
                    }
                }
            }
        }

    }
}