package com.piotrkalin.havira.groupDish.presentation.list

import com.piotrkalin.havira.core.domain.util.toCommonStateFlow
import com.piotrkalin.havira.dish.domain.interactors.DishInteractors
import com.piotrkalin.havira.dish.presentation.list.DishListState
import com.piotrkalin.havira.dish.presentation.list.DishListViewModel
import com.piotrkalin.havira.group.domain.interactors.GroupInteractors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

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
        result.onSuccess { group ->
            println("GroupDishListViewModel: Success")
            return@combine _state.copy(
                dishes = group.dishes,
                filteredDishes = group.dishes,
                searchedDishes = search(group.dishes, _state.searchText),
                sortedDishes = sort(group.dishes, _state.selectedSort, _state.selectedSortDirection),
                isLoading = false,
                groupId = groupId,
                groupName = group.name,
                groupJoinCode = group.joinCode
            )
        }
        result.onFailure { t ->
            println("GroupDishListViewModel: ${result.exceptionOrNull()?.message}")
            return@combine _state.copy(
                error = t.message ?: "Unknown error",
                isLoading = false,
                groupId = groupId
            )
        }
        println("GroupDishListViewModel: Loading")
        _state.copy(
            isLoading = true,
            groupId = groupId
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DishListState())
        .toCommonStateFlow()

    fun onEvent(event: GroupDishListEvent){
        when(event){
            GroupDishListEvent.CloseBottomSheet -> {
                _state.update { it.copy(
                    isBottomSheetOpen = false
                ) }
            }
            GroupDishListEvent.LeaveGroup -> TODO()
            GroupDishListEvent.OpenBottomSheet -> {
                _state.update { it.copy(
                    isBottomSheetOpen = true
                ) }
            }
            GroupDishListEvent.ShareJoinCode -> TODO()
        }
    }
}