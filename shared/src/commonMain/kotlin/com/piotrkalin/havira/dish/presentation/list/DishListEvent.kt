package com.piotrkalin.havira.dish.presentation.list

import com.piotrkalin.havira.core.domain.util.SortDirection
import com.piotrkalin.havira.core.domain.util.SortType

sealed class DishListEvent {
    data class SelectDish(val dishId: Long) : DishListEvent()
    object CreateDish : DishListEvent()
    object OnErrorSeen : DishListEvent()

    //sort and filter
    object OpenSortDropdown: DishListEvent()
    object DismissSortDropdown: DishListEvent()
    data class SelectSortType(val sortType: SortType) : DishListEvent()
    data class SelectSortDirection(val sortDirection: SortDirection) : DishListEvent()
    object ChangeFilterBoxVisibility : DishListEvent()

    //search
    object OpenSearchView : DishListEvent()
    object DismissSearchView : DishListEvent()
    data class SearchDish(val searchString: String) : DishListEvent()
}