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

open class DishEditViewModel(
    private val dishId: Long,
    private val dishInteractors: DishInteractors,
    private val coroutineScope: CoroutineScope?,
    private val loadDish : suspend () -> Result<Dish> = {dishInteractors.getDishById(dishId)}
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
                    isValidDish = isValidDish(dish.id, dish.title),
                    groupId = dish.groupId?.toLong()
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
                if (!_state.value.isValidDish) return
                _state.update { it.copy( isSaving = true ) }
                viewModelScope.launch {
                    val result = editDish(_state.value)
                    result.onFailure { t ->
                        println(t.message)
                        _state.update { it.copy(
                            error = t.message,
                            isSaving = false
                        ) }
                    }
                    result.onSuccess {
                        _state.update { it.copy(
                            isSaving = false
                        ) }
                        event.onEdit()
                    }
                }
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
                    viewModelScope.launch {
                        val result = deleteDish(dishId)
                        result.onSuccess {
                            event.onDelete()
                        }
                        result.onFailure { t ->
                            println(t.message)
                            _state.update { it.copy(
                                error = t.message,
                                isDeleteDialogOpen = false
                            ) }
                        }
                    }
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

    protected open suspend fun editDish(state: DishEditState): Result<Unit?> {
        return dishInteractors.editDish(
            id = state.dishId!!,
            title = state.title,
            description = state.desc
        )
    }

    protected open suspend fun deleteDish(dishId: Long): Result<Unit?> {
        return dishInteractors.deleteDishById(dishId)
    }

    private fun isValidDish(id: Long?, title: String) : Boolean{
        return title.isNotBlank() && id != null
    }
}