package com.piotrkalin.havira.core.data.remote

import io.ktor.client.statement.*

class NotFoundException(
    val httpResponse: HttpResponse? = null,
    override val message : String? = "Not Found Exception"
) : Exception()

class TimeoutException(
    val httpResponse: HttpResponse? = null,
    override val message : String? = "Request Timeout Exception"
) : Exception()