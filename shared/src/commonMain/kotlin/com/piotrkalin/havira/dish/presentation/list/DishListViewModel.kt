package com.piotrkalin.havira.dish.presentation.list

import com.piotrkalin.havira.core.domain.util.SortDirection
import com.piotrkalin.havira.core.domain.util.SortType
import com.piotrkalin.havira.core.domain.util.toCommonStateFlow
import com.piotrkalin.havira.dish.domain.interactors.DishInteractors
import com.piotrkalin.havira.dish.domain.model.Dish
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

class DishListViewModel(
    private val dishInteractors: DishInteractors,
    private val coroutineScope: CoroutineScope?
) {

    private val viewModelScope = coroutineScope ?: CoroutineScope(Dispatchers.Main)

    private val _state = MutableStateFlow(DishListState())
    val state = combine(
        _state,
        dishInteractors.getAllDishes()
    ) { _state, dishes ->
        if (dishes.throwable != null) {
            _state.copy(
                error = dishes.throwable.message ?: "Unknown error"
            )
        }
        else if (_state.dishes != dishes.data) {
            _state.copy(
                dishes = dishes.data ?: emptyList(),
                filteredDishes = dishes.data ?: emptyList(),
                searchedDishes = search(dishes.data ?: emptyList(), _state.searchText),
                sortedDishes = sort(dishes.data ?: emptyList(), _state.selectedSort, _state.selectedSortDirection)
            )
        } else _state
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DishListState())
        .toCommonStateFlow()

    fun onEvent(event: DishListEvent){
        when(event) {
            is DishListEvent.CreateDish -> {}
            is DishListEvent.SelectDish -> {}
            is DishListEvent.OnErrorSeen -> {
                _state.update { it.copy(
                    error = null
                ) }
            }
            is DishListEvent.SearchDish -> {
                _state.update { it.copy(
                    searchText = event.searchString
                ) }
            }
            DishListEvent.DismissSortDropdown -> {
                _state.update { it.copy(
                    isSortingDropdownDisplayed = false
                ) }
            }
            DishListEvent.OpenSortDropdown -> {
                _state.update { it.copy(
                    isSortingDropdownDisplayed = true
                ) }
            }
            is DishListEvent.SelectSortType -> {
                _state.update { it.copy(
                    isSortingDropdownDisplayed = false,
                    selectedSort = event.sortType,
                ) }
            }
            is DishListEvent.SelectSortDirection -> {
                _state.update { it.copy(
                    selectedSortDirection = event.sortDirection,
                ) }
            }
            DishListEvent.ChangeFilterBoxVisibility -> {
                _state.update { it.copy(
                    isFilterBoxVisible = !it.isFilterBoxVisible
                ) }
            }
            DishListEvent.DismissSearchView -> {
                _state.update { it.copy(
                    isSearchViewOpen = false,
                    searchText = ""
                ) }
            }
            DishListEvent.OpenSearchView -> {
                _state.update { it.copy(
                    isSearchViewOpen = true
                ) }
            }
        }
    }

    private fun sort(items: List<Dish>, sortType: SortType, sortDirection: SortDirection) : List<Dish>{
        return if(sortDirection == SortDirection.ASC)
            when(sortType){
                SortType.RATING -> items.sortedBy { it.rating }
                SortType.LAST_MADE -> items.sortedBy { it.lastMade }
                SortType.CREATED -> items.sortedBy { it.created }
                SortType.NOF_RATINGS -> items.sortedBy { it.nofRatings }
                SortType.TITLE -> items.sortedBy { it.title.lowercase() }
            }
        else when(sortType){
            SortType.RATING -> items.sortedByDescending { it.rating }
            SortType.LAST_MADE -> items.sortedByDescending { it.lastMade }
            SortType.CREATED -> items.sortedByDescending { it.created }
            SortType.NOF_RATINGS -> items.sortedByDescending { it.nofRatings }
            SortType.TITLE -> items.sortedByDescending { it.title.lowercase() }
        }
    }

    private fun search(items: List<Dish>, query: String) : List<Dish>{
        return items.filter {
            it.title.contains(query, ignoreCase = true)
            || it.desc.contains(query, ignoreCase = true)
        }
    }
}