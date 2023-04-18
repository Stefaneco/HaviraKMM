package com.piotrkalin.havira.group.domain

import com.piotrkalin.havira.group.data.model.CreateGroupRequest
import com.piotrkalin.havira.group.data.model.GroupResponse

interface IGroupService {

    suspend fun createGroup(request: CreateGroupRequest) : GroupResponse

    suspend fun getGroup(groupId : Long) : GroupResponse

    suspend fun getAllGroups() : List<GroupResponse>

    suspend fun joinGroup(joinCode: String) : GroupResponse

    suspend fun leaveGroup(groupId: Long)

    suspend fun disbandGroup(groupId: Long)
}