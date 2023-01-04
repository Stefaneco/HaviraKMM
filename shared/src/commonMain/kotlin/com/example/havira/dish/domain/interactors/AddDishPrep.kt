package com.example.havira.dish.domain.interactors

import com.example.havira.dish.domain.IDishRepository
import com.example.havira.dish.domain.model.DishPrep

class AddDishPrep(
    private val dishRepository: IDishRepository
) {
    suspend operator fun invoke(dishPrep: DishPrep) : Result<Unit?>{
        return try {
            dishRepository.insertDishPrep(dishPrep)
            Result.success(null)
        } catch (e: Exception){
            Result.failure(e)
        }
    }
}