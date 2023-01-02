package com.example.havira.dish.domain.model

import kotlinx.datetime.LocalDateTime

data class Dish(
    val id: Long? = null,
    val title: String,
    val description: String,
    val rating : Int = 0,
    val created : LocalDateTime
)
