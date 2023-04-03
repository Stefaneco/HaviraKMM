package com.piotrkalin.havira.dish.data

import com.piotrkalin.havira.core.domain.model.Dish
import com.piotrkalin.havira.core.domain.model.DishPrep
import database.DishEntity
import database.DishPrepEntity
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun DishEntity.toDish(): Dish {
    return Dish(
        id = id,
        title = title,
        desc = description,
        created = Instant.fromEpochMilliseconds(created)
            .toLocalDateTime(TimeZone.currentSystemDefault()),
        rating = rating.toFloat(),
        nofRatings = nof_ratings.toInt(),
        lastMade = last_made?.let {
            Instant.fromEpochMilliseconds(it).toLocalDateTime(TimeZone.currentSystemDefault())
        }
    )
}

fun DishPrepEntity.toDishPrep() : DishPrep {
    return DishPrep(
        id = id,
        dishId = dish_id,
        rating = rating.toInt(),
        date = date
    )
}
