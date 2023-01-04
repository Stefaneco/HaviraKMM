package com.example.havira.dish.presentation.detail

import com.example.havira.dish.domain.model.Dish
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

data class DishDetailState(
    val dish : Dish? = null,
/*    val title: String = "",
    val desc: String = "",
    val rating: Int = 0,
    val lastMade : String = "New dish",
    val nofRatings : Int = 0,
    val created : String = "",
    val dishPreps : List<DishPrep> = emptyList(),*/
    val isLoading : Boolean = true,
    val error : String? = null,
    val isAddingDishPrep : Boolean = false,
    val isDishPrepCreatorOpen : Boolean = false,
    val newDishPrepRating : Int = 0,
    val newDishPrepDate : LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
)

/*
Szczegóły dania:
Tytuł - text
Opis - text
Ocena - icon
Kiedy ostatnio zrobione - text
Ile razy zrobione - text
Kiedy dodane - text
Lista dat i ocen:
Data - text
Ocena - icon
Danie zrobione - button*/
