package com.piotrkalin.havira.auth.data

import io.ktor.client.plugins.api.*
import io.ktor.http.*


val AuthPlugin = createClientPlugin("AuthPlugin", ::AuthPluginConfig) {
    val headerName = pluginConfig.headerName

    on(Send) { request ->
        val token = pluginConfig.loadToken()
        if (token != null) {
            request.headers.append(headerName, token)
        }
        val originalCall = proceed(request)
        originalCall.response.run { // this: HttpResponse
            if(status == HttpStatusCode.Unauthorized) {
                val refreshedToken = pluginConfig.refreshToken() ?: throw Exception("AuthPlugin: RefreshToken returned null")
                request.headers.append(headerName, refreshedToken)
                proceed(request)
            } else {
                originalCall
            }
        }
    }
}

class AuthPluginConfig {
    var headerName : String = ""
    var loadToken : suspend () -> String? = { null }
    var refreshToken : suspend () -> String = { "" }
}