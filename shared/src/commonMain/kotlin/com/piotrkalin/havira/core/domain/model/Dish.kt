package com.piotrkalin.havira.core.domain.model

import com.piotrkalin.havira.core.domain.util.DateTimeUtil
import com.piotrkalin.havira.group.data.model.DishListItemDto
import com.piotrkalin.havira.groupDish.data.model.DishResponse
import kotlinx.datetime.LocalDateTime

data class Dish(
    val id: Long? = null,
    val ownerId: String? = null,
    val groupId: Int? = null,
    val title: String,
    val desc: String,
    var rating : Float = 0f,
    var nofRatings : Int = 0,
    var lastMade : LocalDateTime? = null,
    val created : LocalDateTime,
    var dishPreps : List<DishPrep>? = null
) {
    operator fun plus(dishPrep: DishPrep) : Dish {
        val newDishPreps = listOf(dishPrep) + (dishPreps?: emptyList())
        val newLastMade = DateTimeUtil.fromEpochMillis(dishPrep.date)
        val newRating = ((rating * nofRatings) + dishPrep.rating)/(nofRatings + 1)
        val newNofRatings = nofRatings + 1
        return Dish(
            id,
            ownerId,
            groupId,
            title,
            desc,
            newRating,
            newNofRatings,
            newLastMade,
            created,
            newDishPreps
        )
    }

    operator fun plusAssign(dishPrep: DishPrep){
        dishPreps = listOf(dishPrep) + (dishPreps?: emptyList())
        lastMade = DateTimeUtil.fromEpochMillis(dishPrep.date)
        rating = ((rating * nofRatings) + dishPrep.rating)/(nofRatings + 1)
        nofRatings += 1
    }

    companion object {
        fun fromDishListItemDto(dto: DishListItemDto) : Dish {
            return Dish(
                id = dto.id,
                ownerId = dto.ownerId,
                groupId = dto.groupId,
                title = dto.title,
                desc = dto.desc,
                rating = dto.rating,
                nofRatings = dto.nofRatings,
                lastMade = dto.lastMadeTimestamp?.let { DateTimeUtil.fromEpochMillis(it) },
                created = DateTimeUtil.fromEpochMillis(dto.createdTimestamp)
            )
        }

        fun fromDishResponse(dto : DishResponse) : Dish {
            return Dish(
                id = dto.id,
                ownerId = dto.ownerId,
                groupId = dto.groupId,
                title = dto.title,
                desc = dto.desc,
                rating = dto.rating,
                nofRatings = dto.nofRatings,
                lastMade = dto.lastMadeTimestamp?.let { DateTimeUtil.fromEpochMillis(it) },
                created = DateTimeUtil.fromEpochMillis(dto.createdTimestamp),
                dishPreps = dto.dishPreps.map { DishPrep.fromDishPrepResponse(it) }
            )
        }
    }
}
