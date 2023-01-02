package com.example.havira.dish.domain.errors

class DishNotFoundException(
    override val message : String = "Dish not found!"
) : Exception() {
}