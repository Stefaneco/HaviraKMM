package com.piotrkalin.havira.dish.domain.interactors

import com.piotrkalin.havira.core.domain.model.Dish

class AddDish(
    private val dishRepository: com.piotrkalin.havira.dish.domain.IDishRepository
) {
    suspend operator fun invoke(dish: Dish) : Result<Unit?>{
        return try {
            dishRepository.insertDish(dish)
            Result.success(null)
        } catch (e: Exception){
            Result.failure(e)
        }

    }
}