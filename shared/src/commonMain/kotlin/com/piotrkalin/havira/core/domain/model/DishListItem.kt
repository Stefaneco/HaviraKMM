package com.piotrkalin.havira.core.domain.model

import com.piotrkalin.havira.core.domain.util.DateTimeUtil
import com.piotrkalin.havira.group.data.model.DishListItemDto
import kotlinx.datetime.LocalDateTime

data class DishListItem(
    val id: Long? = null,
    val ownerId: String? = null,
    val groupId: Int? = null,
    val title: String,
    val desc: String,
    var rating : Float = 0f,
    var nofRatings : Int = 0,
    var lastMade : LocalDateTime? = null,
    val created : LocalDateTime
) {
    companion object {
        fun fromDishListItemDto(dto: DishListItemDto) : DishListItem {
            return DishListItem(
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
    }
}