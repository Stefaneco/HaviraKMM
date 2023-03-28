package com.piotrkalin.havira.groupDish.domain

import com.piotrkalin.havira.groupDish.data.model.CreateDishRequest
import com.piotrkalin.havira.groupDish.data.model.DishResponse

interface IDishService {

    suspend fun createGroupDish(request : CreateDishRequest) : DishResponse
}