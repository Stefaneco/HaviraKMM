package com.piotrkalin.havira.groupDish.domain.interactors

import com.piotrkalin.havira.core.domain.model.Dish
import com.piotrkalin.havira.core.domain.model.DishPrep
import com.piotrkalin.havira.groupDish.domain.IDishService

class AddGroupDishPrep(
    private val dishService: IDishService
) {
    suspend operator fun invoke(dishPrep: DishPrep, dish : Dish) : Result<Dish> {
        return try {
            if (dishPrep.rating !in 1..5) {
                throw IllegalArgumentException("Rating must be between 1 and 5")
            }
            val response = dishService.addGroupDishPrep(dishPrep.toAddDishPrepRequest(), dishPrep.dishId)
            val updatedDish = dish + DishPrep.fromDishPrepResponse(response)
            updatedDish.dishPreps = updatedDish.dishPreps?.sortedByDescending { it.date }
            Result.success(updatedDish)
        } catch (e: Exception){
            Result.failure(e)
        }
    }
}