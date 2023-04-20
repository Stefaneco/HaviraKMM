package com.piotrkalin.havira.groupDish.data.model

@kotlinx.serialization.Serializable
data class UpdateDishRequest(
    val title: String,
    val desc: String
)