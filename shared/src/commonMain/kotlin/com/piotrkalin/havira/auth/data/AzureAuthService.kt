package com.piotrkalin.havira.auth.data

import com.piotrkalin.havira.auth.data.model.AzureLoginResponse
import com.piotrkalin.havira.auth.data.model.GoogleTokens
import com.piotrkalin.havira.auth.domain.IAuthService
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class AzureAuthService(
    private val httpClient: HttpClient
) : IAuthService {

    override suspend fun getAuthTokenWithGoogle(idToken: String) : String {
        println("AzureAuthService login started")
        val response = httpClient.post("https://havira-api.azurewebsites.net/.auth/login/google"){
            contentType(ContentType.Application.Json)
            setBody(GoogleTokens(idToken))
        }
        println("AzureAuthService response: ${response}")
        println("AzureAuthService response body: ${response.body<String>()}")
        return response.body<AzureLoginResponse>().authenticationToken
    }

    override suspend fun logout() {
        httpClient.get("https://havira-api.azurewebsites.net/.auth/logout")
    }
}