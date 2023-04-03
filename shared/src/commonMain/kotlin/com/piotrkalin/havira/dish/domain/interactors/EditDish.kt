package com.piotrkalin.havira.dish.domain.interactors

class EditDish(
    private val dishRepository: com.piotrkalin.havira.dish.domain.IDishRepository
) {
    suspend operator fun invoke(id: Long, title: String, description: String) : Result<Unit?>{
        return try {
            dishRepository.updateDish(id, title, description)
            Result.success(null)
        } catch (e: Exception){
            Result.failure(e)
        }
    }
}