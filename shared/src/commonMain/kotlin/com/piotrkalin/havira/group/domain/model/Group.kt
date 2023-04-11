package com.piotrkalin.havira.group.domain.model

import com.piotrkalin.havira.auth.domain.model.UserProfile
import com.piotrkalin.havira.core.domain.model.Dish
import com.piotrkalin.havira.core.domain.util.DateTimeUtil
import com.piotrkalin.havira.group.data.model.CreateGroupRequest
import com.piotrkalin.havira.group.data.model.GroupResponse
import database.GroupEntity
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

data class Group(
    val ownerId: String,
    val id: Long,
    val dishes : List<Dish> = emptyList(),
    val joinCode : String,
    val name : String,
    val created: LocalDateTime,
    val members: List<UserProfile> = emptyList()
){
    fun toCreateGroupRequest() : CreateGroupRequest =
        CreateGroupRequest(name)

    fun toGroupEntity() : GroupEntity {
        return GroupEntity(id, ownerId, joinCode, name, created.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds())
    }

    companion object {
        fun fromGroupResponse(groupResponse: GroupResponse) : Group {
            return Group(
                ownerId = groupResponse.ownerId,
                id = groupResponse.id,
                dishes = groupResponse.dishes.map { Dish.fromDishListItemDto(it) },
                joinCode = groupResponse.joinCode,
                name = groupResponse.name,
                created = DateTimeUtil.fromEpochMillis(groupResponse.createdTimestamp)
            )
        }
    }
}
