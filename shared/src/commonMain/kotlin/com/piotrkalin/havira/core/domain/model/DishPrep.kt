package com.piotrkalin.havira.core.domain.model

data class DishPrep(
    val id: Long? = null,
    val userId : String? = null,
    val dishId: Long,
    val rating: Int,
    val date: Long
)
