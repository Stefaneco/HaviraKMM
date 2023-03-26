package com.piotrkalin.havira.group.presentation.join

sealed class JoinGroupEvent {
    object OnErrorSeen : JoinGroupEvent()
    object BackButtonPressed : JoinGroupEvent()
    data class EditJoinCode(val code : String) : JoinGroupEvent()
    object JoinGroup : JoinGroupEvent()
    object NavigateToJoinedGroup : JoinGroupEvent()
}