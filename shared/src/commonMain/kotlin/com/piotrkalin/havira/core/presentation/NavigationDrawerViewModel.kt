package com.piotrkalin.havira.core.presentation

import com.piotrkalin.havira.core.domain.util.toCommonStateFlow
import com.piotrkalin.havira.group.domain.interactors.GroupInteractors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class NavigationDrawerViewModel(
    private val groupInteractors: GroupInteractors,
    private val coroutineScope: CoroutineScope?
) {

    private val viewModelScope = coroutineScope ?: CoroutineScope(Dispatchers.Main)

    private val _state = MutableStateFlow(NavigationDrawerState())
    val state = _state.asStateFlow().toCommonStateFlow()

    /*val state = combine(
        _state,
        groupInteractors.getAllGroups()
    ){ _state, groups ->
        if (groups.isSuccess) {
            _state.copy(
                groups = groups.getOrElse { emptyList() }
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NavigationDrawerState())
        .toCommonStateFlow()*/

    init {
        println("NavigationDrawerViewModel: init")
        viewModelScope.launch {
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
        }
    }
}