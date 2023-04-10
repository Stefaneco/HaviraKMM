package com.piotrkalin.havira.core.presentation

import com.piotrkalin.havira.auth.domain.interactors.AuthInteractors
import com.piotrkalin.havira.core.domain.util.toCommonStateFlow
import com.piotrkalin.havira.group.domain.interactors.GroupInteractors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class NavigationDrawerViewModel(
    private val groupInteractors: GroupInteractors,
    private val authInteractors: AuthInteractors,
    private val coroutineScope: CoroutineScope?
) {

    private val viewModelScope = coroutineScope ?: CoroutineScope(Dispatchers.Main)

    private val _state = MutableStateFlow(NavigationDrawerState())
    val state = _state.asStateFlow().toCommonStateFlow()

    init {
        println("NavigationDrawerViewModel: init")
        refreshGroups()
    }

    fun onEvent(event: NavigationDrawerEvent){
        when(event){
            NavigationDrawerEvent.CreateGroup -> {
                _state.update { it.copy(
                    isDrawerOpen = false
                ) }
            }
            NavigationDrawerEvent.JoinGroup -> {
                _state.update { it.copy(
                    isDrawerOpen = false
                ) }
            }
            is NavigationDrawerEvent.NavigateToGroup -> {
                _state.update { it.copy(
                    isSoloSelected = false,
                    selectedGroup = event.id
                ) }
            }
            NavigationDrawerEvent.NavigateToSettings -> {
                _state.update { it.copy(
                    isDrawerOpen = false
                ) }
            }
            NavigationDrawerEvent.NavigateToSolo -> {
                _state.update { it.copy(
                    selectedGroup = null,
                    isSoloSelected = true
                ) }
            }
            NavigationDrawerEvent.CloseDrawer -> {
                _state.update { it.copy(
                    isDrawerOpen = false
                ) }
            }
            NavigationDrawerEvent.OpenDrawer -> {
                _state.update { it.copy(
                    isDrawerOpen = true
                ) }
            }
            NavigationDrawerEvent.OnGroupAdded -> {
                refreshGroups()
            }
        }
    }

    private fun refreshGroups(){
        println("NavigationDrawerViewModel: refresh groups")
        viewModelScope.launch {
            val isUserLoggedIn = authInteractors.isUserLoggedIn()
            println("NavigationDrawerViewModel isUserLoggedIn: $isUserLoggedIn")
            _state.update { it.copy(
                isUserLoggedIn = isUserLoggedIn
            ) }
            if (!isUserLoggedIn) return@launch
            groupInteractors.getAllGroups().collect { result ->
                if (result.isSuccess){
                    _state.update { it.copy(
                        groups = result.getOrElse { emptyList() }
                    ) }
                }
                else if (result.isFailure){
                    println("NavigationDrawerViewModel: Failed to load groups")
                }
            }
        }
    }
}