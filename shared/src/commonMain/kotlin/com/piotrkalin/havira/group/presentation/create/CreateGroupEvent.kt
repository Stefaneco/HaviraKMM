package com.piotrkalin.havira.group.presentation.create

sealed class CreateGroupEvent {
    object OnErrorSeen : CreateGroupEvent()
    object BackButtonPressed : CreateGroupEvent()
    data class EditGroupName(val name: String) : CreateGroupEvent()
    object CreateGroup : CreateGroupEvent()
    object NavigateToCreatedGroup : CreateGroupEvent()
}