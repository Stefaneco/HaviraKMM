package com.piotrkalin.havira.auth.presentation

data class LoginScreenState(
    val error : String? = null,
    val loginLoading : Boolean = false
)