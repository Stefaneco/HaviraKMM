package com.piotrkalin.havira.group.data.model

@kotlinx.serialization.Serializable
data class CreateGroupRequest(
    val name: String
)