package com.piotrkalin.havira.groupDish.data.model

@kotlinx.serialization.Serializable
data class CreateDishRequest(
    val groupId: Long,
    val title : String,
    val desc: String
)