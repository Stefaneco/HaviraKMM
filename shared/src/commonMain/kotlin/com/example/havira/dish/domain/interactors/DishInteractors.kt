package com.example.havira.dish.interactors

import com.example.havira.dish.domain.interactors.AddDish
import com.example.havira.dish.domain.interactors.DeleteDishById
import com.example.havira.dish.domain.interactors.GetAllDishes
import com.example.havira.dish.domain.interactors.GetDishById

data class DishInteractors(
    val addDish: AddDish,
    val deleteDishById: DeleteDishById,
    val getAllDishes: GetAllDishes,
    val getDishById: GetDishById
)
