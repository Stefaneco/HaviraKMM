package com.example.havira.dish.presentation.list

import com.example.havira.core.domain.util.SortDirection
import com.example.havira.core.domain.util.SortType
import com.example.havira.core.domain.util.toCommonStateFlow
import com.example.havira.dish.domain.model.Dish
import com.example.havira.dish.interactors.DishInteractors
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
                val filteredDishes = _state.value.dishes.filter { it.title.startsWith(event.searchString) }
                _state.update { it.copy(
                    filteredDishes = filteredDishes
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
                    sortedDishes = sort(it.filteredDishes, event.sortType, it.selectedSortDirection)
                ) }
            }
            is DishListEvent.SelectSortDirection -> {
                _state.update { it.copy(
                    selectedSortDirection = event.sortDirection,
                    sortedDishes = sort(it.filteredDishes, it.selectedSort, event.sortDirection)
                ) }
            }
            DishListEvent.ChangeFilterBoxVisibility -> {
                _state.update { it.copy(
                    isFilterBoxVisible = !it.isFilterBoxVisible
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
                SortType.TITLE -> items.sortedBy { it.title }
            }
        else when(sortType){
            SortType.RATING -> items.sortedByDescending { it.rating }
            SortType.LAST_MADE -> items.sortedByDescending { it.lastMade }
            SortType.CREATED -> items.sortedByDescending { it.created }
            SortType.NOF_RATINGS -> items.sortedByDescending { it.nofRatings }
            SortType.TITLE -> items.sortedByDescending { it.title }
        }
    }
}