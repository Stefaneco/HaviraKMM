package com.example.havira.dish.presentation.list

import com.example.havira.core.domain.util.toCommonStateFlow
import com.example.havira.dish.interactors.DishInteractors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

class DishListViewModel(
    private val dishInteractors: DishInteractors,
    private val coroutineScope: CoroutineScope?
) {

    private val viewModelScope = coroutineScope ?: CoroutineScope(Dispatchers.Main)

    private val _state = MutableStateFlow(DishListState())
    val state = combine(
        _state,
        dishInteractors.getAllDishes()
    ) { _state, dishes ->
        if (dishes.throwable != null) {
            _state.copy(
                error = dishes.throwable.message ?: "Unknown error"
            )
        }
        else if (_state.dishes != dishes.data) {
            _state.copy(
                dishes = dishes.data ?: emptyList()
            )
        } else _state
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DishListState())
        .toCommonStateFlow()

    fun onEvent(event: DishListEvent){
        when(event) {
            is DishListEvent.CreateDish -> {

            }
            is DishListEvent.SelectDish -> {

            }
            is DishListEvent.OnErrorSeen -> {
                _state.update { it.copy(
                    error = null
                ) }
            }
        }
    }
}