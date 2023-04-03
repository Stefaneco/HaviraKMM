package com.piotrkalin.havira.core.presentation

sealed class NavigationDrawerEvent {
    object NavigateToSettings : NavigationDrawerEvent()
    object JoinGroup : NavigationDrawerEvent()
    object CreateGroup : NavigationDrawerEvent()
    data class NavigateToGroup(val id: Long) : NavigationDrawerEvent()
    object NavigateToSolo : NavigationDrawerEvent()
    object CloseDrawer : NavigationDrawerEvent()
    object OpenDrawer : NavigationDrawerEvent()
}