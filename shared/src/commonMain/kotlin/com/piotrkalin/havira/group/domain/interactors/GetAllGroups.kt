package com.piotrkalin.havira.group.domain.interactors

import com.piotrkalin.havira.core.domain.util.CommonFlow
import com.piotrkalin.havira.core.domain.util.toCommonFlow
import com.piotrkalin.havira.group.domain.IGroupService
import com.piotrkalin.havira.group.domain.model.Group
import kotlinx.coroutines.flow.flow

class GetAllGroups(
    private val groupService: IGroupService
) {
    operator fun invoke() : CommonFlow<Result<List<Group>>> = flow {
        try {
            println("GetAllGroups: Started request")
            val response = groupService.getAllGroups()
            println("GetAllGroups: $response")
            emit(Result.success(response.map { Group.fromGroupResponse(it) }))
        } catch (e: Exception){
            println("GetAllGroups error: ${e.message}")
            emit(Result.failure(e))
        }

    }.toCommonFlow()
}