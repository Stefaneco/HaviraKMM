package com.piotrkalin.havira.group.domain.interactors

import com.piotrkalin.havira.core.domain.util.CommonFlow
import com.piotrkalin.havira.core.domain.util.toCommonFlow
import com.piotrkalin.havira.group.domain.IGroupService
import com.piotrkalin.havira.group.domain.model.Group
import kotlinx.coroutines.flow.flow

class JoinGroup(
    private val groupService: IGroupService
) {
    operator fun invoke(joinCode: String) : CommonFlow<Result<Group>> = flow {
        try{
            val response = groupService.joinGroup(joinCode)
            emit(Result.success(Group.fromGroupResponse(response)))
        }catch (e: Exception){
            emit(Result.failure(e))
        }
    }.toCommonFlow()
}