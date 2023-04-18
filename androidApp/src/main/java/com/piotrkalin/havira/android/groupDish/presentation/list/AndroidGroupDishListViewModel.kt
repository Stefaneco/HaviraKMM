package com.piotrkalin.havira.android.groupDish.presentation.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.piotrkalin.havira.auth.domain.interactors.AuthInteractors
import com.piotrkalin.havira.dish.domain.interactors.DishInteractors
import com.piotrkalin.havira.dish.presentation.list.DishListEvent
import com.piotrkalin.havira.group.domain.interactors.GroupInteractors
import com.piotrkalin.havira.groupDish.presentation.list.GroupDishListEvent
import com.piotrkalin.havira.groupDish.presentation.list.GroupDishListViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AndroidGroupDishListViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val groupInteractors: GroupInteractors,
    private val dishInteractors: DishInteractors,
    private val authInteractors: AuthInteractors
) : ViewModel() {

    private val groupId = savedStateHandle.get<String>("groupId")?.toLong() ?: -1L
    private val viewModel by lazy {
        println("AndroidGroupDishListViewModel groupId: $groupId")
        GroupDishListViewModel(
            groupId, groupInteractors, dishInteractors, authInteractors ,viewModelScope
        )
    }

    val state = viewModel.state

    fun onEvent(event: DishListEvent) = viewModel.onEvent(event)

    fun onEvent(event: GroupDishListEvent) = viewModel.onEvent(event)
}