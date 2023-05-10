package com.piotrkalin.havira.dish.domain.interactors

import com.piotrkalin.havira.dish.domain.IDishRepository

class DeleteDishPrepById(
    private val dishRepository: IDishRepository
) {
    suspend operator fun invoke(id: Long) : Result<Unit?> {
        return try {
            dishRepository.deleteDishPrepById(id)
            Result.success(null)
        } catch (e: Exception){
            Result.failure(e)
        }
    }
}