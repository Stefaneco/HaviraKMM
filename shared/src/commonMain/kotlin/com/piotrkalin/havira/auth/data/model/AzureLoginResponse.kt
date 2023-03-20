package com.piotrkalin.havira.auth.data.model

@kotlinx.serialization.Serializable
data class AzureLoginResponse(
    val authenticationToken: String, val user: AzureUserFromTokenResponse
) {
}

@kotlinx.serialization.Serializable
data class AzureUserFromTokenResponse(
    val userId:String
)