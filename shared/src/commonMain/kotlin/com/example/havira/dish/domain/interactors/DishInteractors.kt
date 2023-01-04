package com.example.havira.dish.interactors

import com.example.havira.dish.domain.interactors.*

data class DishInteractors(
    val addDish: AddDish,
    val deleteDishById: DeleteDishById,
    val getAllDishes: GetAllDishes,
    val getDishById: GetDishById,
    val addDishPrep: AddDishPrep
)
