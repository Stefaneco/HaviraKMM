package com.piotrkalin.havira.core.data.remote

import com.piotrkalin.havira.auth.data.AuthPlugin
import com.piotrkalin.havira.auth.domain.ITokenDataSource
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class KtorClientFactory(
    private val engine: HttpClientEngine,
    private val tokenDataSource: ITokenDataSource
) {
    private fun buildAnonymousClient() : HttpClient {
        val client = HttpClient(engine) {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
        }
        return client
    }

    fun build() : HttpClient {
        val client = buildAnonymousClient()

        val authClient = HttpClient(engine) {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
            install(AuthPlugin) {
                headerName = "X-ZUMO-AUTH"
                loadToken = { tokenDataSource.getAuthToken() }
                refreshToken = {
                    val oldToken = tokenDataSource.getAuthToken()
                        ?: throw Exception("Session not found on the device!")
                    val newToken = client.get(""){
                        headers {
                            append("X-ZUMO-AUTH", oldToken)
                        }
                    }.body<String>()
                    newToken
                }
            }
        }

        return authClient
    }
}