package com.piotrkalin.havira.dish.domain.interactors

import com.piotrkalin.havira.core.domain.model.Dish
import com.piotrkalin.havira.core.domain.util.CommonFlow
import com.piotrkalin.havira.core.domain.util.Resource
import com.piotrkalin.havira.core.domain.util.toCommonFlow
import kotlinx.coroutines.flow.flow

class GetDishById(
  private val dishRepository: com.piotrkalin.havira.dish.domain.IDishRepository
) {
    operator fun invoke(id: Long) : CommonFlow<Resource<Dish>> = flow {

        try {
            val dish = dishRepository.getDishById(id)
            if(dish == null) emit(Resource.Error(throwable = com.piotrkalin.havira.dish.domain.errors.DishNotFoundException()))
            else emit(Resource.Success(dish))
        } catch (e: Exception){
            emit(Resource.Error(e))
        }

    }.toCommonFlow()
}