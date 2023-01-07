package com.example.havira.dish.domain.model

import com.example.havira.core.domain.util.DateTimeUtil
import kotlinx.datetime.LocalDateTime

data class Dish(
    val id: Long? = null,
    val title: String,
    val desc: String,
    var rating : Float = 0f,
    var nofRatings : Int = 0,
    var lastMade : LocalDateTime? = null,
    val created : LocalDateTime,
    var dishPreps : List<DishPrep>? = null
) {
    operator fun plus(dishPrep: DishPrep) : Dish{
        val newDishPreps = listOf(dishPrep) + (dishPreps?: emptyList())
        val newLastMade = DateTimeUtil.fromEpochMillis(dishPrep.date)
        val newRating = ((rating * nofRatings) + dishPrep.rating)/(nofRatings + 1)
        val newNofRatings = nofRatings + 1
        return Dish(id,title, desc, newRating, newNofRatings, newLastMade, created, newDishPreps)
    }

    operator fun plusAssign(dishPrep: DishPrep){
        dishPreps = listOf(dishPrep) + (dishPreps?: emptyList())
        lastMade = DateTimeUtil.fromEpochMillis(dishPrep.date)
        rating = ((rating * nofRatings) + dishPrep.rating)/(nofRatings + 1)
        nofRatings += 1
    }
}
