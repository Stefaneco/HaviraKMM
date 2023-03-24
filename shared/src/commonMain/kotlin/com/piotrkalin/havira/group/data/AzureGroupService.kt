package com.piotrkalin.havira.group.data

import com.piotrkalin.havira.group.data.model.CreateGroupRequest
import com.piotrkalin.havira.group.data.model.GroupResponse
import com.piotrkalin.havira.group.domain.IGroupService
import io.ktor.client.*
import io.ktor.client.call.*
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
        TODO("Not yet implemented")
    }

    override suspend fun getAllGroups(): List<GroupResponse> {
        val response = httpClient.get("https://havira-api.azurewebsites.net/api/Group")
        return response.body()
    }

}