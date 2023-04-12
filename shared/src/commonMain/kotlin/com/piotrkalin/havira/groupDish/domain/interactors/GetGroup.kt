package com.piotrkalin.havira.groupDish.domain.interactors

import com.piotrkalin.havira.core.data.remote.TimeoutException
import com.piotrkalin.havira.core.domain.util.CommonFlow
import com.piotrkalin.havira.core.domain.util.toCommonFlow
import com.piotrkalin.havira.group.domain.IGroupService
import com.piotrkalin.havira.group.domain.model.Group
import io.ktor.util.network.*
import kotlinx.coroutines.flow.flow

class GetGroup(
    private val groupService: IGroupService
) {
    operator fun invoke(groupId: Long) : CommonFlow<Result<Group>> = flow {
        try {
            val result = groupService.getGroup(groupId)
            println("GetGroup: $result")
            emit(Result.success(Group.fromGroupResponse(result)))
        }
        catch (e: UnresolvedAddressException) {
            emit(Result.failure(TimeoutException(message = "No internet connection")))
        }
        catch (e: Exception){
            emit(Result.failure(e))
        }
    }.toCommonFlow()
}