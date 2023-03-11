package com.piotrkalin.havira.dish.domain.interactors

import com.piotrkalin.havira.dish.domain.IDishRepository
import com.piotrkalin.havira.dish.domain.model.Dish

class AddDish(
    private val dishRepository: IDishRepository
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