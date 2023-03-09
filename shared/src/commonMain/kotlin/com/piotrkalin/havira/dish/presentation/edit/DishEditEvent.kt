package com.piotrkalin.havira.dish.presentation.edit

sealed class DishEditEvent {
    data class EditTitle(val title: String) : DishEditEvent()
    data class EditDescription(val description: String) : DishEditEvent()
    class EditDish(val onEdit: () -> Unit = {}) : DishEditEvent()
    object BackButtonPressed : DishEditEvent()
    object OnErrorSeen : DishEditEvent()
    class DeleteDish(val onDelete: () -> Unit = {}) : DishEditEvent()
    object DismissDeleteDialog : DishEditEvent()
    object OpenDeleteDialog : DishEditEvent()
}