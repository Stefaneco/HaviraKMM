package com.piotrkalin.havira.groupDish.data.model

@kotlinx.serialization.Serializable
data class DishResponse(
    val id: Long? = null,
    val ownerId: String? = null,
    val groupId: Int? = null,
    val title: String,
    val desc: String,
    var rating : Float = 0f,
    var nofRatings : Int = 0,
    var lastMade : Long? = null,
    val created : Long,
    val dishPreps : List<DishPrepDto> = emptyList()
)