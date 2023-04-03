package com.piotrkalin.havira.dish.domain.interactors

import com.piotrkalin.havira.core.domain.model.Dish
import com.piotrkalin.havira.core.domain.model.DishPrep
import com.piotrkalin.havira.dish.domain.IDishRepository

class AddDishPrep(
    private val dishRepository: IDishRepository
) {
    /*
    Inserts into database new dishPrep and updates rating, nofRatings, lastMade of Dish
    Gets new dishPrep database id
    Adds new dishPrep to Dish and returns Dish
     */
    suspend operator fun invoke(dishPrep: DishPrep, dish: Dish) : Result<Dish>{
        return try {
            val newDishPrepId = dishRepository.insertDishPrep(dishPrep, dish + dishPrep)
            val newDishPrep = dishPrep.copy(
                id = newDishPrepId
            )
            val newDish = dish + newDishPrep
            Result.success(newDish)
        } catch (e: Exception){
            Result.failure(e)
        }
    }
}