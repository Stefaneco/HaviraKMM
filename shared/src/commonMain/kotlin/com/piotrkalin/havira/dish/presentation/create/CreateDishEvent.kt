package com.piotrkalin.havira.dish.presentation.create

sealed class CreateDishEvent {
    data class EditTitle(val title: String) : CreateDishEvent()
    data class EditDescription(val description: String) : CreateDishEvent()
    class CreateDish(val onCreate: () -> Unit = {}) : CreateDishEvent()
    object OnErrorSeen : CreateDishEvent()
    object BackButtonPressed : CreateDishEvent()
}