package com.example.havira.dish.presentation.create

data class CreateDishState(
    val title: String = "",
    val desc: String = "",
    val isValidDish: Boolean = false,
    val error : String? = null,
    val isCreating: Boolean = false
)