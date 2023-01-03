package com.example.havira.dish.domain.model

import kotlinx.datetime.LocalDateTime

data class Dish(
    val id: Long? = null,
    val title: String,
    val desc: String,
    val rating : Int = 0,
    val nofRatings : Int = 0,
    val lastMade : LocalDateTime? = null,
    val created : LocalDateTime
)
