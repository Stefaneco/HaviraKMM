package com.piotrkalin.havira.group.presentation.join

import com.piotrkalin.havira.group.domain.model.Group

data class JoinGroupState (
    val joinCode: String = "",
    val error : String? = null,
    val isValidCode : Boolean = false,
    val isJoining : Boolean = false,
    val isJoined : Boolean = false,
    val group : Group? = null
)