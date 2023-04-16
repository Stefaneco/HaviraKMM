package com.piotrkalin.havira.group.data

import com.piotrkalin.havira.group.data.model.CreateGroupRequest
import com.piotrkalin.havira.group.data.model.GroupResponse
import com.piotrkalin.havira.group.domain.IGroupService
import kotlinx.coroutines.delay

class FakeGroupService : IGroupService {

    private val groups = mutableListOf<GroupResponse>()
    private var nextGroupId = 1L
    var ownerId = "fakeUser"
    var throwsError = false
    var errorMessage = "Group service error"

    override suspend fun createGroup(request: CreateGroupRequest): GroupResponse {
        delay(500) // Simulate network delay
        if(throwsError) throw Exception(errorMessage)
        val group = GroupResponse(
            ownerId = ownerId,
            id = nextGroupId++,
            joinCode = "joinCode${nextGroupId}",
            name = request.name,
            createdTimestamp = System.currentTimeMillis()
        )
        groups.add(group)
        return group
    }

    override suspend fun getGroup(groupId: Long): GroupResponse {
        delay(500) // Simulate network delay
        if(throwsError) throw Exception(errorMessage)
        return groups.find { it.id == groupId }
            ?: throw Exception("Group not found")
    }

    override suspend fun getAllGroups(): List<GroupResponse> {
        delay(500) // Simulate network delay
        if(throwsError) throw Exception(errorMessage)
        return groups.toList()
    }

    override suspend fun joinGroup(joinCode: String): GroupResponse {
        delay(500) // Simulate network delay
        if(throwsError) throw Exception(errorMessage)
        return groups.find { it.joinCode == joinCode }
            ?: throw Exception("Group not found")
    }
}