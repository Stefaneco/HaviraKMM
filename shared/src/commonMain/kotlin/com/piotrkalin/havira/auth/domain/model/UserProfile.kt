package com.piotrkalin.havira.auth.domain.model

@kotlinx.serialization.Serializable
data class UserProfile (
    val id: String,
    val name: String
        )