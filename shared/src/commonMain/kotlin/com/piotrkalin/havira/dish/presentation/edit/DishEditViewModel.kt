package com.piotrkalin.havira.dish.presentation.edit

import com.piotrkalin.havira.core.domain.model.Dish
import com.piotrkalin.havira.core.domain.util.toCommonStateFlow
import com.piotrkalin.havira.dish.domain.interactors.DishInteractors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DishEditViewModel(
    private val dishId: Long,
    private val dishInteractors: DishInteractors,
    private val coroutineScope: CoroutineScope?
) {

    private val viewModelScope = coroutineScope ?: CoroutineScope(Dispatchers.Main)

    private val _state = MutableStateFlow(DishEditState())
    val state = _state.asStateFlow().toCommonStateFlow()

    init {
        viewModelScope.launch {
            val result = loadDish()
            result.onSuccess { dish ->
                _state.update { it.copy(
                    dishId = dishId,
                    title = dish.title,
                    desc = dish.desc,
                    isValidDish = isValidDish(dish.id, dish.title)
                ) }
            }
            result.onFailure { t ->
                _state.update { it.copy(
                    error = t.message ?: "Unknown error",
                ) }
            }
        }
    }

    fun onEvent(event: DishEditEvent){
        when(event){
            DishEditEvent.BackButtonPressed -> TODO()
            is DishEditEvent.EditDescription -> {
                _state.update { it.copy(
                    desc = event.description
                ) }
            }
            is DishEditEvent.EditDish -> {
                editDish(_state.value, event.onEdit)
            }
            is DishEditEvent.EditTitle -> {
                _state.update { it.copy(
                    title = event.title,
                    isValidDish = isValidDish(it.dishId ,event.title)
                ) }
            }
            DishEditEvent.OnErrorSeen -> {
                _state.update { it.copy(
                    error = null
                ) }
            }
            is DishEditEvent.DeleteDish -> {
                _state.value.dishId?.let { dishId ->
                    deleteDish(dishId, event.onDelete)
                }
            }
            DishEditEvent.DismissDeleteDialog -> {
                _state.update { it.copy(
                    isDeleteDialogOpen = false
                ) }
            }
            DishEditEvent.OpenDeleteDialog -> {
                _state.update { it.copy(
                    isDeleteDialogOpen = true
                ) }
            }
        }
    }

    protected suspend fun loadDish() : Result<Dish> {
        return dishInteractors.getDishById(dishId)
    }

    private fun editDish(state: DishEditState, onEdit: () -> Unit){
        if (!state.isValidDish) return
        viewModelScope.launch {
            _state.update { it.copy( isSaving = true ) }

            val result = dishInteractors.editDish(
                id = state.dishId!!,
                title = state.title,
                description = state.desc
            )

            if(result.isFailure) {
                println(result.exceptionOrNull()?.message)
                _state.update { it.copy(
                    error = result.exceptionOrNull()?.message,
                    isSaving = false
                ) }
            }
            else if(result.isSuccess){
                _state.update { it.copy(
                    isSaving = false
                ) }
                onEdit()
            }
        }
    }

    private fun deleteDish(dishId: Long, onDelete: () -> Unit){
        viewModelScope.launch {

            val result = dishInteractors.deleteDishById(dishId)

            if(result.isFailure){
                println(result.exceptionOrNull()?.message)
                _state.update { it.copy(
                    error = result.exceptionOrNull()?.message
                ) }
            }
            else if(result.isSuccess){
                onDelete()
            }
        }
    }

    private fun isValidDish(id: Long?, title: String) : Boolean{
        return title.isNotBlank() && id != null
    }
}