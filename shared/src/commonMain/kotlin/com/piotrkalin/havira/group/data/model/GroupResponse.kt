package com.piotrkalin.havira.group.data.model

@kotlinx.serialization.Serializable
data class GroupResponse(
    val ownerId: String,
    val id: Long,
    val dishes : List<DishListItemDto> = emptyList(),
    val joinCode : String,
    val name : String,
    val createdTimestamp: Long
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
    var lastMade : Long? = null,
    val created : Long
)