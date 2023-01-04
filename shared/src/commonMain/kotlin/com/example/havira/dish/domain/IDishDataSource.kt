package com.example.havira.dish.domain

import com.example.havira.dish.domain.model.Dish
import com.example.havira.dish.domain.model.DishPrep

interface IDishDataSource {
    suspend fun getAllDishes(): List<Dish>
    suspend fun getDishById(id: Long) : Dish?
    suspend fun deleteDishById(id: Long)
    suspend fun insertDish(dish: Dish)
    suspend fun insertDishPrep(dishPrep: DishPrep)
}