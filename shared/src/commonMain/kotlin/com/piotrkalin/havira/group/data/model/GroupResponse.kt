package com.piotrkalin.havira.group.data.model

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class GroupResponse(
    val ownerId: String,
    val id: Long,
    @SerialName("dishListItemDtos")
    val dishes : List<DishListItemDto> = emptyList(),
    val joinCode : String,
    val name : String,
    val createdTimestamp: Long,
)

@kotlinx.serialization.Serializable
data class DishListItemDto(
    val id: Long? = null,
    val ownerId: String? = null,
    val groupId: Int? = null,
    val title: String,
    val desc: String,
    var rating : Float = 0f,
    var nofRatings : Int = 0,
    var lastMadeTimestamp : Long? = null,
    val createdTimestamp : Long
)