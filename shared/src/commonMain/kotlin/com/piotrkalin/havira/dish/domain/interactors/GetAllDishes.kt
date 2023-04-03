package com.piotrkalin.havira.dish.domain.interactors

import com.piotrkalin.havira.core.domain.model.Dish
import com.piotrkalin.havira.core.domain.util.CommonFlow
import com.piotrkalin.havira.core.domain.util.Resource
import com.piotrkalin.havira.core.domain.util.toCommonFlow
import kotlinx.coroutines.flow.flow

class GetAllDishes(
    private val dishRepository: com.piotrkalin.havira.dish.domain.IDishRepository
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