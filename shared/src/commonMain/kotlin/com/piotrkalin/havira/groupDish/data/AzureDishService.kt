package com.piotrkalin.havira.groupDish.data

import com.piotrkalin.havira.groupDish.data.model.CreateDishRequest
import com.piotrkalin.havira.groupDish.data.model.DishResponse
import com.piotrkalin.havira.groupDish.domain.IDishService
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class AzureDishService(
    private val httpClient: HttpClient
) : IDishService {

    override suspend fun createGroupDish(request: CreateDishRequest): DishResponse {
        val response = httpClient.post("https://havira-api.azurewebsites.net/api/Dish"){
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        println("CreateGroupDish status: ${response.status}")
        println("CreateGroupDish status: ${response.body<String>()}")
        return response.body()
    }

}