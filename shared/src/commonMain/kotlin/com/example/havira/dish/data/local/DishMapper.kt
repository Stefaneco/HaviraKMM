package com.example.havira.dish.data.local

import com.example.havira.dish.domain.model.Dish
import database.DishEntity
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun DishEntity.toDish(): Dish {
    return Dish(
        id = id,
        title = title,
        description = description,
        created = Instant.fromEpochMilliseconds(created).toLocalDateTime(TimeZone.currentSystemDefault()),
        rating = rating.toInt()
    )
}