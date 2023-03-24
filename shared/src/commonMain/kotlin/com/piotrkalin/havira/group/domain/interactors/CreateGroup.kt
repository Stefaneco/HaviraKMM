package com.piotrkalin.havira.group.domain.interactors

import com.piotrkalin.havira.core.domain.util.CommonFlow
import com.piotrkalin.havira.core.domain.util.toCommonFlow
import com.piotrkalin.havira.group.domain.IGroupService
import com.piotrkalin.havira.group.domain.model.Group
import kotlinx.coroutines.flow.flow

class CreateGroup(
    private val groupService: IGroupService
) {
    suspend operator fun invoke(group: Group) : CommonFlow<Result<Group>> = flow {
        try {
            val response = groupService.createGroup(group.toCreateGroupRequest())
            emit(Result.success(Group.fromGroupResponse(response)))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.toCommonFlow()
}