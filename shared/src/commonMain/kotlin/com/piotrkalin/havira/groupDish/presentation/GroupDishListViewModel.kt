package com.piotrkalin.havira.groupDish.presentation

import com.piotrkalin.havira.core.domain.util.toCommonStateFlow
import com.piotrkalin.havira.dish.domain.interactors.DishInteractors
import com.piotrkalin.havira.dish.presentation.list.DishListState
import com.piotrkalin.havira.dish.presentation.list.DishListViewModel
import com.piotrkalin.havira.group.domain.interactors.GroupInteractors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class GroupDishListViewModel(
    private val groupId: Long,
    private val groupInteractors: GroupInteractors,
    private val dishInteractors: DishInteractors,
    private val coroutineScope: CoroutineScope?
) : DishListViewModel(dishInteractors, coroutineScope) {

    override val state = combine(
        _state,
        groupInteractors.getGroup(groupId)
    ) { _state, result ->
       if(result.isSuccess) {
           println("GroupDishListViewModel: Success")
           _state.copy(
               dishes = result.getOrNull()?.dishes ?: emptyList() ,
               filteredDishes = result.getOrNull()?.dishes ?: emptyList(),
               searchedDishes = search(result.getOrNull()?.dishes ?: emptyList(), _state.searchText),
               sortedDishes = sort(result.getOrNull()?.dishes ?: emptyList(), _state.selectedSort, _state.selectedSortDirection),
               isLoading = false,
               groupId = groupId
           )
       }
        else if (result.isFailure){
           println("GroupDishListViewModel: ${result.exceptionOrNull()?.message}")
            _state.copy(
                error = result.exceptionOrNull()?.message,
                isLoading = false,
                groupId = groupId
            )
       }
        else {
           println("GroupDishListViewModel: Success")
            _state.copy(
                isLoading = true,
                groupId = groupId
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DishListState())
        .toCommonStateFlow()
}