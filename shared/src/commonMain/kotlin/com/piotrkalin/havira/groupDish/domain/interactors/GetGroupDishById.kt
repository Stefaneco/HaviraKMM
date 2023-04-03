package com.piotrkalin.havira.groupDish.domain.interactors

import com.piotrkalin.havira.core.domain.model.Dish
import com.piotrkalin.havira.groupDish.domain.IDishService

class GetGroupDishById(
    private val dishService: IDishService
) {
    suspend operator fun invoke(dishId: Long) : Result<Dish> {
        return try {
            val response = dishService.getGroupDishById(dishId)
            println("GetGroupDishById response: $response")
            val dish = Dish.fromDishResponse(response)
            dish.dishPreps = dish.dishPreps?.sortedByDescending { it.date }
            Result.success(dish)
        } catch (e: Exception){
            Result.failure(e)
        }
    }
}