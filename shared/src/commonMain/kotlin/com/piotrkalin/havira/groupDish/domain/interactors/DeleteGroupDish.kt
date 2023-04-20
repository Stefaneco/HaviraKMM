package com.piotrkalin.havira.groupDish.domain.interactors

import com.piotrkalin.havira.groupDish.domain.IDishService

class DeleteGroupDish(
    private val dishService: IDishService
) {
    suspend operator fun invoke(dishId: Long) : Result<Nothing?> {
        return try {
            dishService.deleteGroupDish(dishId)
            Result.success(null)
        } catch (e: Exception){
            Result.failure(e)
        }
    }
}