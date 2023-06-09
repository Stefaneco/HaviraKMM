package com.piotrkalin.havira.dish.presentation.edit

data class DishEditState(
    val dishId: Long? = null,
    val title: String = "",
    val desc: String = "",
    val isValidDish: Boolean = false,
    val error : String? = null,
    val isSaving: Boolean = false,
    val isDeleteDialogOpen: Boolean = false,

    //group
    val groupId: Long? = null
) {
}