package com.piotrkalin.havira.dish.domain.interactors

import com.piotrkalin.havira.core.domain.model.Dish
import com.piotrkalin.havira.dish.domain.IDishRepository
import com.piotrkalin.havira.dish.domain.errors.DishNotFoundException

class GetDishById(
  private val dishRepository: IDishRepository
) {
    suspend operator fun invoke(id: Long) : Result<Dish> {
        try {
            val dish = dishRepository.getDishById(id) ?: return Result.failure(DishNotFoundException())
            return Result.success(dish)
        } catch (e: Exception){
            return Result.failure(e)
        }
    }
}