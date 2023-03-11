package com.piotrkalin.havira.dish.domain.model

data class DishPrep(
    val id: Long? = null,
    val dishId: Long,
    val rating: Int,
    val date: Long
)
