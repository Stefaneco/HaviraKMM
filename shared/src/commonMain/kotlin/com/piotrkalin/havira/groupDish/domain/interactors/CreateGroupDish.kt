package com.piotrkalin.havira.groupDish.domain.interactors

import com.piotrkalin.havira.core.domain.model.Dish
import com.piotrkalin.havira.groupDish.data.model.CreateDishRequest
import com.piotrkalin.havira.groupDish.domain.IDishService

class CreateGroupDish(
    private val dishService: IDishService
) {
    suspend operator fun invoke(createGroupDishRequest: CreateDishRequest) : Result<Dish> {
        return try {
            val response = dishService.createGroupDish(createGroupDishRequest)
            println("CreateGroupDish response: $response")
            Result.success(Dish.fromDishResponse(response))
        } catch (e: Exception){
            println("CreateGroupDish error: ${e.message}")
            Result.failure(e)
        }
    }
}