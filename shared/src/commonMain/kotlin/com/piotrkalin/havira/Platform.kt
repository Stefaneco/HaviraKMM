package com.piotrkalin.havira

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform