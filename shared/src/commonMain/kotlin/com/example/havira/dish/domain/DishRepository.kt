package com.example.havira.dish.domain

import com.example.havira.dish.domain.model.Dish

class DishRepository(
    private val localDishDataSource: IDishDataSource
): IDishRepository {

    override suspend fun getAllDishes(): List<Dish> = localDishDataSource.getAllDishes()

    override suspend fun getDishById(id: Long): Dish? = localDishDataSource.getDishById(id)

    override suspend fun deleteDishById(id: Long) = localDishDataSource.deleteDishById(id)

    override suspend fun insertDish(dish: Dish) = localDishDataSource.insertDish(dish)

}