package com.piotrkalin.havira.auth.data

import com.piotrkalin.havira.auth.domain.IProfileService
import com.piotrkalin.havira.auth.domain.model.UserProfile
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*

class AzureProfileService(
    private val httpClient: HttpClient
) : IProfileService {

    override suspend fun getUserProfile(): UserProfile {
        val response = httpClient.get("https://havira-api.azurewebsites.net/api/Profile")
        //if(response.status == HttpStatusCode.NotFound) return null
        return response.body()
    }

    override suspend fun getUserProfileOrNull(): UserProfile? {
        val response = httpClient.get("https://havira-api.azurewebsites.net/api/Profile")
        println("Auth AzureProfileService getUserProfileOrNull: $response")
        println("Auth AzureProfileService getUserProfileOrNull: ${response.body<String>()}")
        if(response.status == HttpStatusCode.NotFound) return null
        return response.body()
    }

    override suspend fun createUserProfile(name: String, image : ByteArray?) : UserProfile {
        val response = httpClient.submitFormWithBinaryData(
            url = "https://havira-api.azurewebsites.net/api/Profile",
            formData = formData {
                append("name", name)
                image?.let {
                    append("image", image, Headers.build {
                        append(HttpHeaders.ContentType, "image/jpeg")
                        append(HttpHeaders.ContentDisposition, "filename=\"profile_pic.jpeg\"")
                    })
                }
            }
        )
        println("Auth AzureProfileService createUserProfile: $response")
        println("Auth AzureProfileService createUserProfile: ${response.body<String>()}")
        return response.body()
    }

    override suspend fun uploadPhoto(image: ByteArray): String {
        val response = httpClient.submitFormWithBinaryData(
            url = "https://havira-api.azurewebsites.net/api/Profile/Photo",
            formData = formData {
                append("image", image, Headers.build {
                    append(HttpHeaders.ContentType, "image/jpg")
                    append(HttpHeaders.ContentDisposition, "filename=\"profile_pic.jpg\"")
                })
            }
        )
        return response.body()
    }
}