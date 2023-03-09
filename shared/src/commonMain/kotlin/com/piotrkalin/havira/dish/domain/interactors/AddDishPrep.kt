package com.piotrkalin.havira.dish.domain.interactors

import com.piotrkalin.havira.core.domain.util.Resource
import com.piotrkalin.havira.dish.domain.IDishRepository
import com.piotrkalin.havira.dish.domain.model.Dish
import com.piotrkalin.havira.dish.domain.model.DishPrep

class AddDishPrep(
    private val dishRepository: IDishRepository
) {
    /*
    Inserts into database new dishPrep and updates rating, nofRatings, lastMade of Dish
    Gets new dishPrep database id
    Adds new dishPrep to Dish and returns Dish
     */
    suspend operator fun invoke(dishPrep: DishPrep, dish: Dish) : Resource<Dish>{
        return try {
            val newDishPrepId = dishRepository.insertDishPrep(dishPrep, dish + dishPrep)
            val newDishPrep = dishPrep.copy(
                id = newDishPrepId
            )
            val newDish = dish + newDishPrep
            return Resource.Success(newDish)
        } catch (e: Exception){
            Resource.Error(e)
        }
    }
}