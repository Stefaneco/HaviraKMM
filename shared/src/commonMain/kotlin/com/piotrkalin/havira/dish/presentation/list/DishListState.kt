package com.piotrkalin.havira.dish.presentation.list

import com.piotrkalin.havira.core.domain.model.Dish
import com.piotrkalin.havira.core.domain.util.SortDirection
import com.piotrkalin.havira.core.domain.util.SortType

data class DishListState(
    val isLoading : Boolean = true,
    val dishes : List<Dish> = emptyList(),

    //group
    val groupId : Long? = null,
    val groupName : String? = null,
    val groupJoinCode : String? = null,
    val userId : String? = null,
    val isUserGroupOwner : Boolean = false,

    //bottomSheet
    val isBottomSheetOpen : Boolean = false,

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