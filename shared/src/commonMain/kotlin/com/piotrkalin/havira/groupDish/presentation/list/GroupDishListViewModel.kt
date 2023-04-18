package com.piotrkalin.havira.groupDish.presentation.list

import com.piotrkalin.havira.auth.domain.interactors.AuthInteractors
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
import kotlinx.coroutines.launch

class GroupDishListViewModel(
    private val groupId: Long,
    private val groupInteractors: GroupInteractors,
    private val dishInteractors: DishInteractors,
    private val authInteractors: AuthInteractors,
    private val coroutineScope: CoroutineScope?
) : DishListViewModel(dishInteractors, coroutineScope) {

    init {
        val profileId = authInteractors.getUserProfileId()
        _state.update { it.copy(
            userId = profileId
        ) }
    }

    override val state = combine(
        _state,
        groupInteractors.getGroup(groupId)
    ) { _state, result ->
        result.onSuccess { group ->
            println("GroupDishListViewModel: Success - $group")
            return@combine _state.copy(
                dishes = group.dishes,
                filteredDishes = group.dishes,
                searchedDishes = search(group.dishes, _state.searchText),
                sortedDishes = sort(group.dishes, _state.selectedSort, _state.selectedSortDirection),
                isLoading = false,
                groupId = groupId,
                groupName = group.name,
                groupJoinCode = group.joinCode,
                isUserGroupOwner = group.ownerId == _state.userId
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
            is GroupDishListEvent.LeaveGroup -> {
                println("GroupDishListViewModel: Started leaving group with id ${state.value.groupId}")
                state.value.groupId?.let { groupId ->
                    viewModelScope.launch {
                        val result = groupInteractors.leaveGroup(groupId)
                        result.onFailure { t ->
                            _state.update { it.copy(
                                error = t.message
                            ) }
                        }
                        result.onSuccess {
                            println("GroupDishListViewModel: Disband Successful")
                            event.onSuccess()
                        }
                    }
                }
            }
            GroupDishListEvent.OpenBottomSheet -> {
                _state.update { it.copy(
                    isBottomSheetOpen = true
                ) }
            }
            GroupDishListEvent.ShareJoinCode -> TODO()
            is GroupDishListEvent.DisbandGroup -> {
                println("GroupDishListViewModel: Started disband with id ${state.value.groupId}")
                state.value.groupId?.let { groupId ->
                    viewModelScope.launch {
                        val result = groupInteractors.disbandGroup(groupId)
                        result.onFailure { t ->
                            _state.update { it.copy(
                                error = t.message
                            ) }
                        }
                        result.onSuccess {
                            println("GroupDishListViewModel: Disband Successful")
                            event.onSuccess()
                        }
                    }
                }

            }
        }
    }
}