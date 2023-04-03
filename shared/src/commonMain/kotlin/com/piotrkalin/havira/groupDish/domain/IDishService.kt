package com.piotrkalin.havira.groupDish.domain

import com.piotrkalin.havira.groupDish.data.model.AddDishPrepRequest
import com.piotrkalin.havira.groupDish.data.model.CreateDishRequest
import com.piotrkalin.havira.groupDish.data.model.DishPrepDto
import com.piotrkalin.havira.groupDish.data.model.DishResponse

interface IDishService {

    suspend fun createGroupDish(request : CreateDishRequest) : DishResponse

    suspend fun addGroupDishPrep(request: AddDishPrepRequest, dishId: Long) : DishPrepDto

    suspend fun getGroupDishById(dishId : Long) : DishResponse
}