package com.piotrkalin.havira.groupDish.data.model

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class DishPrepDto (
    val id: Long,
    @SerialName("userProfileId")
    val userId : String,
    val dishId: Long,
    val rating: Int,
    val dateTimestamp: Long
    )