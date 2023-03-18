package com.piotrkalin.havira.auth.data.model

@kotlinx.serialization.Serializable
data class GoogleTokens(
    val id_token: String,
    //val authorization_code: String,
    //val redirect_uri: String = "https://havira-api.azurewebsites.net/.auth/login/google/callback"
)