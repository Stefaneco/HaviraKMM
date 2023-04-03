package com.piotrkalin.havira.dish.presentation.detail

import com.piotrkalin.havira.core.domain.model.Dish
import com.piotrkalin.havira.core.domain.util.DateTimeUtil
import com.piotrkalin.havira.core.domain.util.toCommonStateFlow
import com.piotrkalin.havira.core.domain.model.DishPrep
import com.piotrkalin.havira.dish.domain.interactors.DishInteractors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

open class DishDetailViewModel(
    private val dishId : Long,
    private val dishInteractors: DishInteractors,
    private val coroutineScope: CoroutineScope?,
    private val loadDish : suspend (Long) -> Result<Dish> = {
        dishInteractors.getDishById.invoke(dishId)
    },
) {

    private val viewModelScope = coroutineScope ?: CoroutineScope(Dispatchers.Main)

    private val _state = MutableStateFlow(DishDetailState())
    val state = _state.asStateFlow().toCommonStateFlow()

    init {
        viewModelScope.launch {
            val result = loadDish(dishId)
            println("DishDetailViewModel result: ${result.getOrNull()}")
            result.onSuccess { dish ->
                val rating = (dish.rating).toInt()
                _state.update { it.copy(
                    dish = dish,
                    isLoading = false,
                    newDishPrepRating = if(rating != 0) rating else 4
                ) }
            }
            result.onFailure { t ->
                _state.update { it.copy(
                    error = t.message ?: "Unknown error",
                    isLoading = false
                ) }
            }
        }
    }

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
                    val dish = _state.value.dish ?: return@launch
                    val result = addDishPrep(
                        DishPrep(
                            rating = _state.value.newDishPrepRating,
                            date = DateTimeUtil.toEpochMillis(_state.value.newDishPrepDate),
                            dishId = dishId),
                        dish
                    )
                    result.onSuccess { updatedDish ->
                        _state.update { it.copy(
                            dish = updatedDish
                        ) }
                    }
                    result.onFailure { t ->
                        println("DishDetailViewModel error: ${t.message}")
                        _state.update { it.copy(
                            error = t.message
                        ) }
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
            DishDetailEvent.OnErrorSeen -> {
                _state.update { it.copy(
                    error = null
                ) }
            }
        }
    }

    protected open suspend fun addDishPrep(dishPrep : DishPrep, dish: Dish): Result<Dish> {
        return dishInteractors.addDishPrep(dishPrep, dish)
    }
}