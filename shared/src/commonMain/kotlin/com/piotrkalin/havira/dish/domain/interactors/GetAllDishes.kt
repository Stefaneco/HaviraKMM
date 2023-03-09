package com.piotrkalin.havira.dish.domain.interactors

import com.piotrkalin.havira.core.domain.util.CommonFlow
import com.piotrkalin.havira.core.domain.util.Resource
import com.piotrkalin.havira.core.domain.util.toCommonFlow
import com.piotrkalin.havira.dish.domain.IDishRepository
import com.piotrkalin.havira.dish.domain.model.Dish
import kotlinx.coroutines.flow.flow

class GetAllDishes(
    private val dishRepository: IDishRepository
) {
    operator fun invoke() : CommonFlow<Resource<List<Dish>>> = flow {
        try{
            val dishes = dishRepository.getAllDishes()
            emit(Resource.Success(dishes))
        } catch (e: Exception){
            emit(Resource.Error(e))
        }

    }.toCommonFlow()
}