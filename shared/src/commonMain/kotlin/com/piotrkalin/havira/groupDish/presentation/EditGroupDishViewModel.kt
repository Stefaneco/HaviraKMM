package com.piotrkalin.havira.groupDish.presentation

import com.piotrkalin.havira.dish.domain.interactors.DishInteractors
import com.piotrkalin.havira.dish.presentation.edit.DishEditState
import com.piotrkalin.havira.dish.presentation.edit.DishEditViewModel
import kotlinx.coroutines.CoroutineScope

class EditGroupDishViewModel(
    private val dishId: Long,
    private val dishInteractors: DishInteractors,
    private val coroutineScope: CoroutineScope?
) : DishEditViewModel(dishId, dishInteractors, coroutineScope, {dishInteractors.getGroupDishById(dishId)}){

    override suspend fun deleteDish(dishId: Long): Result<Unit?> {
        return dishInteractors.deleteGroupDish(dishId)
    }

    override suspend fun editDish(state: DishEditState): Result<Unit?> {
        return dishInteractors.editGroupDish(dishId, state.title, state.desc)
    }
}