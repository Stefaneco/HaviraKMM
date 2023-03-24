package com.piotrkalin.havira.group.domain

import com.piotrkalin.havira.group.data.model.CreateGroupRequest
import com.piotrkalin.havira.group.data.model.GroupResponse

interface IGroupService {

    suspend fun createGroup(request: CreateGroupRequest) : GroupResponse

    suspend fun getGroup(groupId : Long) : GroupResponse
}