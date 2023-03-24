package com.piotrkalin.havira.core.presentation

import com.piotrkalin.havira.group.domain.model.Group

data class NavigationDrawerState (
    val groups: List<Group> = emptyList(),
    val selectedGroup : Long? = null,
    val isSoloSelected : Boolean = true,
    val isDrawerOpen : Boolean = false
    )