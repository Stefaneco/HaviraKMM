package com.piotrkalin.havira.dish.domain.interactors

class DeleteDishById(
    private val dishRepository: com.piotrkalin.havira.dish.domain.IDishRepository
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