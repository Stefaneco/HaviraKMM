package com.piotrkalin.havira.dish.domain

import com.piotrkalin.havira.dish.domain.model.Dish
import com.piotrkalin.havira.dish.domain.model.DishPrep

interface IDishDataSource {
    suspend fun getAllDishes(): List<Dish>
    suspend fun getDishById(id: Long) : Dish?
    suspend fun deleteDishById(id: Long)
    suspend fun insertDish(dish: Dish)
    suspend fun insertDishPrep(dishPrep: DishPrep, dish: Dish) : Long
    suspend fun updateDish(id: Long, title: String, description: String)
}