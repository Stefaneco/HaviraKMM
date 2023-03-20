package com.piotrkalin.havira.auth.data.model

@kotlinx.serialization.Serializable
data class AzureTokens(
    val access_token: String,
    val refresh_token: String,
    val id_token: String
)