package com.piotrkalin.havira.dish.domain.interactors

import com.piotrkalin.havira.groupDish.domain.interactors.CreateGroupDish

data class DishInteractors(
    val addDish: AddDish,
    val deleteDishById: DeleteDishById,
    val getAllDishes: GetAllDishes,
    val getDishById: GetDishById,
    val addDishPrep: AddDishPrep,
    val editDish: EditDish,
    val createGroupDish: CreateGroupDish
)
