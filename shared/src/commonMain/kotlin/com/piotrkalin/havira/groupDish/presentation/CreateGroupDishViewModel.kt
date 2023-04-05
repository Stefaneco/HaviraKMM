package com.piotrkalin.havira.groupDish.presentation

import com.piotrkalin.havira.dish.domain.interactors.DishInteractors
import com.piotrkalin.havira.dish.presentation.create.CreateDishState
import com.piotrkalin.havira.dish.presentation.create.CreateDishViewModel
import com.piotrkalin.havira.groupDish.data.model.CreateDishRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateGroupDishViewModel(
    private val groupId: Long,
    private val dishInteractors: DishInteractors,
    private val coroutineScope: CoroutineScope?
) : CreateDishViewModel(dishInteractors, coroutineScope) {

    init {
        _state.update { it.copy(
            groupId = groupId
        ) }
    }

    override fun createDish(state: CreateDishState, onCreate: () -> Unit) {
        if (!state.isValidDish) return
        if (state.groupId == null) {
            _state.update { it.copy(
                error = "Could not find group ID. This should not have happened."
            ) }
            println("CreateGroupDishViewModel error: Could not find group ID. This should not have happened.")
            return
        }
        _state.update { it.copy( isCreating = true ) }
        viewModelScope.launch {
            val result = dishInteractors.createGroupDish(
                CreateDishRequest(state.groupId, state.title, state.desc)
            )
            result.onSuccess {
                println("CreateGroupDishViewModel: create dish success")
                onCreate()
            }
            result.onFailure { t ->
                println("CreateGroupDishViewModel error: create dish error ${t.message}")
                _state.update { it.copy(
                    error = t.message,
                    isCreating = false
                ) }
            }
        }
    }
}