package com.example.havira.dish.domain.interactors

import com.example.havira.dish.domain.IDishRepository
import com.example.havira.dish.domain.model.Dish

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