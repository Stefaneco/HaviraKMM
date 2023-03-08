package com.example.havira.dish.presentation.list

import com.example.havira.core.domain.util.SortDirection
import com.example.havira.core.domain.util.SortType
import com.example.havira.dish.domain.model.Dish

data class DishListState(
    val isLoading : Boolean = true,
    val dishes : List<Dish> = emptyList(),

    //sort and filter
    val filteredDishes: List<Dish> = emptyList(),
    val sortedDishes: List<Dish> = emptyList(),
    val error : String? = null,
    val selectedSort: SortType = SortType.LAST_MADE,
    val selectedSortDirection: SortDirection = SortDirection.DESC,
    val minRating: Float? = null,
    val maxRating: Float? = null,
    val minNofRatings: Int? = null,
    val maxNofRatings: Int? = null,
    val isSortingDropdownDisplayed: Boolean = false,
    val isFilterBoxVisible: Boolean = false,

    //search
    val isSearchViewOpen: Boolean = false,
    val searchText: String = "",
    val searchedDishes: List<Dish> = emptyList()
) {
}