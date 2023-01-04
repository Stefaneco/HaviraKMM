package com.example.havira.android.dish.details.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.havira.dish.presentation.detail.DishDetailEvent
import com.example.havira.dish.presentation.detail.DishDetailState

@Composable
fun DishDetailScreen(
    state : DishDetailState,
    onEvent : (DishDetailEvent) -> Unit
) {
    Column() {

    }
}




















/*
Szczegóły dania:
Tytuł - text, textCard?
Opis - text, textCard?
Ocena - icon, pill?
Kiedy ostatnio zrobione - text, ???
Kiedy dodane - text, ???
Ile razy zrobione - text, ???
Lista dat i ocen:
    Data - text
    Ocena - icon
Danie zrobione - button, Floating centered button?
*/
