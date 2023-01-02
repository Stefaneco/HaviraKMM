package com.example.havira.dish.presentation.create

import com.example.havira.core.domain.util.toCommonStateFlow
import com.example.havira.dish.domain.model.Dish
import com.example.havira.dish.interactors.DishInteractors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class CreateDishViewModel(
    private val dishInteractors: DishInteractors,
    private val coroutineScope: CoroutineScope?
) {

    private val viewModelScope = coroutineScope ?: CoroutineScope(Dispatchers.Main)

    private val _state = MutableStateFlow(CreateDishState())
    val state = _state.asStateFlow().toCommonStateFlow()

    fun onEvent(event : CreateDishEvent){
        when(event) {
            is CreateDishEvent.EditTitle -> {
                _state.update { it.copy(
                    title = event.title,
                    isValidDish = isValidDish(event.title)
                ) }
            }
            is CreateDishEvent.EditDescription -> {
                _state.update { it.copy(
                    desc = event.description
                ) }
            }
            is CreateDishEvent.CreateDish -> {
                createDish(_state.value, event.onCreate)
            }
            is CreateDishEvent.OnErrorSeen -> {

            }
        }
    }

    private fun createDish(state: CreateDishState, onCreate: () -> Unit){
        if (!state.isValidDish) return
        viewModelScope.launch {
           _state.update { it.copy( isCreating = true ) }

            val result = dishInteractors.addDish(Dish(
                title = state.title,
                desc = state.desc,
                created = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())))

            if (result.isFailure) {
                _state.update { it.copy(
                    error = result.exceptionOrNull()?.message,
                    isCreating = false
                ) }
            }
            else if (result.isSuccess){
                _state.update { it.copy(
                    isCreating = false
                ) }
                onCreate()
            }
        }
    }

    private fun isValidDish(title: String) : Boolean{
        return title.isNotBlank()
    }
}