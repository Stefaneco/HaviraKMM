package com.piotrkalin.havira.group.data

import com.piotrkalin.havira.group.data.model.CreateGroupRequest
import com.piotrkalin.havira.group.data.model.GroupResponse
import com.piotrkalin.havira.group.domain.IGroupService
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.http.*

class AzureGroupService(
    private val httpClient: HttpClient
) : IGroupService {

    override suspend fun createGroup(request: CreateGroupRequest): GroupResponse {
        val response = httpClient.post("https://havira-api.azurewebsites.net/api/Group") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        return response.body()
    }

    override suspend fun getGroup(groupId: Long): GroupResponse {
        val response = httpClient.get("https://havira-api.azurewebsites.net/api/Group/$groupId")
        println("AzureGroupService: $response")
        println("AzureGroupService: ${response.body<String>()}")
        return response.body()
    }

    override suspend fun getAllGroups(): List<GroupResponse> {
        val response = httpClient.get("https://havira-api.azurewebsites.net/api/Group") {
            timeout {
                requestTimeoutMillis = 25000
            }
        }
        return response.body()
    }

    override suspend fun joinGroup(joinCode: String): GroupResponse {
        val response = httpClient.get("https://havira-api.azurewebsites.net/api/Group/join/${joinCode}")
        return response.body()
    }

    override suspend fun leaveGroup(groupId: Long) {
        httpClient.put("https://havira-api.azurewebsites.net/api/Group/$groupId/leave")
    }

    override suspend fun disbandGroup(groupId: Long) {
        httpClient.delete("https://havira-api.azurewebsites.net/api/Group/$groupId")
    }

}