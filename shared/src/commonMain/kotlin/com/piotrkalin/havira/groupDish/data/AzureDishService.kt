package com.piotrkalin.havira.groupDish.data

import com.piotrkalin.havira.groupDish.data.model.*
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
        println("AzureDishService CreateGroupDish status: ${response.status}")
        println("AzureDishService CreateGroupDish body: ${response.body<String>()}")
        return response.body()
    }

    override suspend fun addGroupDishPrep(request: AddDishPrepRequest, dishId: Long): DishPrepDto {
        val response = httpClient.post("https://havira-api.azurewebsites.net/api/Dish/${dishId}/DishPrep"){
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        println("AzureDishService AddGroupDishPrep status: ${response.status}")
        println("AzureDishService AddGroupDishPrep body: ${response.body<String>()}")
        return response.body()
    }

    override suspend fun getGroupDishById(dishId: Long): DishResponse {
        val response = httpClient.get("https://havira-api.azurewebsites.net/api/Dish/${dishId}")
        println("AzureDishService getGroupDishById status: ${response.status}")
        println("AzureDishService getGroupDishById body: ${response.body<String>()}")
        return response.body()
    }

    override suspend fun updateGroupDish(request: UpdateDishRequest, dishId: Long): DishResponse {
        val response = httpClient.put("https://havira-api.azurewebsites.net/api/Dish/${dishId}"){
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        println("AzureDishService UpdateGroupDish status: ${response.status}")
        println("AzureDishService UpdateGroupDish body: ${response.body<String>()}")
        return response.body()
    }

    override suspend fun deleteGroupDish(dishId: Long) {
        httpClient.delete("https://havira-api.azurewebsites.net/api/Dish/${dishId}")
    }

}