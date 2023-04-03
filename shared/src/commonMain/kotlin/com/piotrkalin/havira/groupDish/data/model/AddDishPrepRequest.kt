package com.piotrkalin.havira.groupDish.data.model

@kotlinx.serialization.Serializable
data class AddDishPrepRequest(
    val rating: Int
)