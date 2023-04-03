package com.piotrkalin.havira.groupDish.domain.interactors

import com.piotrkalin.havira.core.domain.model.Dish
import com.piotrkalin.havira.core.domain.util.CommonFlow
import com.piotrkalin.havira.core.domain.util.toCommonFlow
import com.piotrkalin.havira.groupDish.data.model.CreateDishRequest
import com.piotrkalin.havira.groupDish.domain.IDishService
import kotlinx.coroutines.flow.flow

class CreateGroupDish(
    private val dishService: IDishService
) {
    operator fun invoke(createGroupDishRequest: CreateDishRequest) : CommonFlow<Result<Dish>> = flow {
        try {
            val response = dishService.createGroupDish(createGroupDishRequest)
            println("CreateGroupDish response: $response")
            emit(Result.success(Dish.fromDishResponse(response)))
        } catch (e: Exception){
            println("CreateGroupDish error: ${e.message}")
            emit(Result.failure(e))
        }
    }.toCommonFlow()
}