package com.piotrkalin.havira.groupDish.presentation

import com.piotrkalin.havira.core.domain.model.Dish
import com.piotrkalin.havira.core.domain.model.DishPrep
import com.piotrkalin.havira.dish.domain.interactors.DishInteractors
import com.piotrkalin.havira.dish.presentation.detail.DishDetailViewModel
import kotlinx.coroutines.CoroutineScope

class GroupDishDetailViewModel(
    private val dishId: Long,
    private val dishInteractors: DishInteractors,
    private val coroutineScope: CoroutineScope?
) : DishDetailViewModel(dishId, dishInteractors, coroutineScope, {
    val response = dishInteractors.getGroupDishById.invoke(dishId)
    println("GroupDishDetailViewModel: $response")
    response
} ) {

    override suspend fun addDishPrep(dishPrep: DishPrep, dish: Dish): Result<Dish> {
        return dishInteractors.addGroupDishPrep(dishPrep, dish)
    }
}