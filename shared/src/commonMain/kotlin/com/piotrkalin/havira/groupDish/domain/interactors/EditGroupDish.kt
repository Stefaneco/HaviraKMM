package com.piotrkalin.havira.groupDish.domain.interactors

import com.piotrkalin.havira.groupDish.data.model.UpdateDishRequest
import com.piotrkalin.havira.groupDish.domain.IDishService

class EditGroupDish(
    private val dishService: IDishService
) {
    suspend operator fun invoke(dishId: Long, title: String, desc: String) : Result<Unit?> {
        return try {
            val updatedDishResponse = dishService.updateGroupDish(
                UpdateDishRequest(title, desc), dishId
            )
            //Result.success(Dish.fromDishResponse(updatedDishResponse))
            Result.success(null)
        } catch (e: Exception){
            Result.failure(e)
        }
    }
}