package com.example.havira

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform