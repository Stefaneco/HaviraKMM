package com.piotrkalin.havira.dish.domain.interactors

import com.piotrkalin.havira.core.domain.util.CommonFlow
import com.piotrkalin.havira.core.domain.util.Resource
import com.piotrkalin.havira.core.domain.util.toCommonFlow
import com.piotrkalin.havira.dish.domain.IDishRepository
import com.piotrkalin.havira.dish.domain.errors.DishNotFoundException
import com.piotrkalin.havira.dish.domain.model.Dish
import kotlinx.coroutines.flow.flow

class GetDishById(
  private val dishRepository: IDishRepository
) {
    operator fun invoke(id: Long) : CommonFlow<Resource<Dish>> = flow {

        try {
            val dish = dishRepository.getDishById(id)
            if(dish == null) emit(Resource.Error(throwable = DishNotFoundException()))
            else emit(Resource.Success(dish))
        } catch (e: Exception){
            emit(Resource.Error(e))
        }

    }.toCommonFlow()
}