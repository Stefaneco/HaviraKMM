package com.example.havira.dish.presentation.list

sealed class DishListEvent {
    data class SelectDish(val id: Long) : DishListEvent()
    object CreateDish : DishListEvent()
    object OnErrorSeen : DishListEvent()
}