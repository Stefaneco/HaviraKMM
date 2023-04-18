package com.piotrkalin.havira.group.domain.interactors

import com.piotrkalin.havira.group.domain.IGroupService

class LeaveGroup(
    private val groupService: IGroupService
) {
    suspend operator fun invoke(groupId : Long) : Result<Nothing?> {
        return try {
            groupService.leaveGroup(groupId)
            Result.success(null)
        } catch (e: Exception){
            Result.failure(e)
        }
    }
}