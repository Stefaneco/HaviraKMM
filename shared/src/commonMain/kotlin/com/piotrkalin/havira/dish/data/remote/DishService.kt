package com.piotrkalin.havira.dish.data.remote

import com.piotrkalin.havira.dish.data.remote.model.WeatherResponse
import com.piotrkalin.havira.dish.domain.IDishService
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class DishService(
    private val httpClient: HttpClient
) : IDishService {

    override suspend fun getAllDishes() {
        val response = httpClient.get("https://havira-api.azurewebsites.net/WeatherForecast")
        println("DishService: $response")
        try {
            println("DishService: ${response.body<List<WeatherResponse>>()}")
        } catch (e: Exception) {
            println("DishService: ${e.message}")
        }
    }
}