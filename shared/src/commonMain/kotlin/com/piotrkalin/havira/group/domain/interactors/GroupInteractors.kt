package com.piotrkalin.havira.group.domain.interactors

import com.piotrkalin.havira.groupDish.domain.interactors.GetGroup

data class GroupInteractors(
    val createGroup: CreateGroup,
    val getAllGroups: GetAllGroups,
    val getGroup: GetGroup,
    val joinGroup: JoinGroup
) {
}