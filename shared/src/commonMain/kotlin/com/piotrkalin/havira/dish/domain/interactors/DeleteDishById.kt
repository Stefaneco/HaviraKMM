package com.piotrkalin.havira.dish.domain.interactors

import com.piotrkalin.havira.dish.domain.IDishRepository

class DeleteDishById(
    private val dishRepository: IDishRepository
) {
    suspend operator fun invoke(id: Long) : Result<Unit?>{
        return try {
            dishRepository.deleteDishById(id)
            Result.success(null)
        } catch (e: Exception){
            Result.failure(e)
        }
    }
}