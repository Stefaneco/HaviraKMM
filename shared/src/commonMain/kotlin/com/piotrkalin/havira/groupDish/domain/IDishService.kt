package com.piotrkalin.havira.groupDish.domain

import com.piotrkalin.havira.groupDish.data.model.*

interface IDishService {

    suspend fun createGroupDish(request : CreateDishRequest) : DishResponse

    suspend fun addGroupDishPrep(request: AddDishPrepRequest, dishId: Long) : DishPrepDto

    suspend fun getGroupDishById(dishId : Long) : DishResponse

    suspend fun updateGroupDish(request: UpdateDishRequest, dishId: Long) : DishResponse

    suspend fun deleteGroupDish(dishId: Long)
}