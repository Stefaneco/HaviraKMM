package com.piotrkalin.havira.group.domain.interactors

import com.piotrkalin.havira.group.domain.IGroupService

class DisbandGroup(
    private val groupService: IGroupService
) {
    suspend operator fun invoke(groupId : Long) : Result<Nothing?> {
        return try {
            groupService.disbandGroup(groupId)
            Result.success(null)
        } catch (e: Exception){
            println("DisbandGroup: failed with $e")
            Result.failure(e)
        }
    }
}