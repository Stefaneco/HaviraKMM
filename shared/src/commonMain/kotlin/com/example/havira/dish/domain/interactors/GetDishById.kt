package com.example.havira.dish.domain.interactors

import com.example.havira.core.domain.util.CommonFlow
import com.example.havira.core.domain.util.Resource
import com.example.havira.core.domain.util.toCommonFlow
import com.example.havira.dish.domain.IDishRepository
import com.example.havira.dish.domain.errors.DishNotFoundException
import com.example.havira.dish.domain.model.Dish
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