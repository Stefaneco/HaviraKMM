package com.piotrkalin.havira.dish.domain.interactors

import com.piotrkalin.havira.groupDish.domain.interactors.*

data class DishInteractors(
    val addDish: AddDish,
    val deleteDishById: DeleteDishById,
    val getAllDishes: GetAllDishes,
    val getDishById: GetDishById,
    val addDishPrep: AddDishPrep,
    val editDish: EditDish,
    val createGroupDish: CreateGroupDish,
    val addGroupDishPrep: AddGroupDishPrep,
    val getGroupDishById: GetGroupDishById,
    val editGroupDish: EditGroupDish,
    val deleteGroupDish: DeleteGroupDish
)
