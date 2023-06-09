package com.piotrkalin.havira.dish.domain

import com.piotrkalin.havira.core.domain.model.Dish
import com.piotrkalin.havira.core.domain.model.DishPrep

class DishRepository(
    private val localDishDataSource: IDishDataSource
): IDishRepository {

    override suspend fun getAllDishes(): List<Dish> = localDishDataSource.getAllDishes()

    override suspend fun getDishById(id: Long): Dish? = localDishDataSource.getDishById(id)

    override suspend fun deleteDishById(id: Long) = localDishDataSource.deleteDishById(id)

    override suspend fun deleteDishPrepById(id: Long) = localDishDataSource.deleteDishPrepById(id)

    override suspend fun insertDish(dish: Dish) = localDishDataSource.insertDish(dish)

    override suspend fun insertDishPrep(dishPrep: DishPrep, dish: Dish)
        = localDishDataSource.insertDishPrep(dishPrep, dish)

    override suspend fun updateDish(id: Long, title: String, description: String)
        = localDishDataSource.updateDish(id, title, description)

}