package com.example.havira.dish.presentation.edit

data class DishEditState(
    val dishId: Long? = null,
    val title: String = "",
    val desc: String = "",
    val isValidDish: Boolean = false,
    val error : String? = null,
    val isSaving: Boolean = false
) {
}