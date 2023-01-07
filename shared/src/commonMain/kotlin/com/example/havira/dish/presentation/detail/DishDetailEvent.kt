package com.example.havira.dish.presentation.detail

sealed class DishDetailEvent {
    object Edit : DishDetailEvent()
    object AddDishPrep : DishDetailEvent()
    object OpenDishPrepCreator : DishDetailEvent()
    object CloseDishPrepCreator : DishDetailEvent()
    data class SetDishPrepCreatorRating(val rating: Int) : DishDetailEvent()
}