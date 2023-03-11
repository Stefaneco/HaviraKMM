package com.piotrkalin.havira.dish.presentation.detail

import com.piotrkalin.havira.core.domain.util.DateTimeUtil
import com.piotrkalin.havira.core.domain.util.Resource
import com.piotrkalin.havira.core.domain.util.toCommonStateFlow
import com.piotrkalin.havira.dish.domain.model.DishPrep
import com.piotrkalin.havira.dish.domain.interactors.DishInteractors
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
                viewModelScope.launch {
                    val dish = _state.value.dish ?: return@launch // ?: throw error
                    val dishId = dish.id ?: return@launch // ?: throw error
                    val newDishResource = dishInteractors.addDishPrep(DishPrep
                        (
                            rating = _state.value.newDishPrepRating,
                            date = DateTimeUtil.toEpochMillis(_state.value.newDishPrepDate),
                            dishId = dishId
                        ),
                        dish
                    )
                    when(newDishResource){
                        is Resource.Success -> {
                            _state.update { it.copy(
                                dish = newDishResource.data
                            ) }
                        }
                        is Resource.Error -> {
                            _state.update { it.copy(
                                error = newDishResource.throwable?.message
                            ) }
                        }
                    }
                }
            }
            is DishDetailEvent.SetDishPrepCreatorRating -> {
                _state.update { it.copy(
                    newDishPrepRating = event.rating
                ) }
            }
            is DishDetailEvent.Edit -> {

            }
            is DishDetailEvent.BackButtonPressed -> {}
            is DishDetailEvent.EditButtonPressed -> {}
        }
    }

    fun loadDish(dishId: Long){
        if (_state.value.dish != null) return
        viewModelScope.launch {
            dishInteractors.getDishById(dishId).collect { dishResource ->
                when(dishResource) {
                    is Resource.Success -> {
                        val rating = (dishResource.data?.rating ?: 5).toInt()
                        _state.update { it.copy(
                            dish = dishResource.data,
                            isLoading = false,
                            newDishPrepRating = if(rating != 0) rating else 4
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