package com.example.havira.dish.presentation.list

import com.example.havira.dish.domain.model.Dish

data class DishListState(
    val isLoading : Boolean = true,
    val dishes : List<Dish> = emptyList(),
    val error : String? = null
) {
}