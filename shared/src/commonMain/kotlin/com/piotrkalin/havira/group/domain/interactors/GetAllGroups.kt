package com.piotrkalin.havira.group.domain.interactors

import com.piotrkalin.havira.core.domain.util.CommonFlow
import com.piotrkalin.havira.core.domain.util.toCommonFlow
import com.piotrkalin.havira.group.domain.IGroupDataSource
import com.piotrkalin.havira.group.domain.IGroupService
import com.piotrkalin.havira.group.domain.model.Group
import kotlinx.coroutines.flow.flow

class GetAllGroups(
    private val groupService: IGroupService,
    private val groupDataSource: IGroupDataSource
) {
    operator fun invoke() : CommonFlow<Result<List<Group>>> = flow {
        try {
            val cachedGroups = groupDataSource.getAllGroups()
            emit(Result.success(cachedGroups))

            println("GetAllGroups: Started request")
            val remoteGroups = groupService.getAllGroups().map { Group.fromGroupResponse(it) }
            println("GetAllGroups: $remoteGroups")
            emit(Result.success(remoteGroups))

            val remoteGroupsIds = remoteGroups.map { it.id }
            val removedGroupsIds = cachedGroups
                .filter { remoteGroupsIds.contains(it.id) }
                .map { it.id }

            groupDataSource.deleteGroupsByIds(removedGroupsIds)
            groupDataSource.insertGroups(remoteGroups)

        } catch (e: Exception){
            println("GetAllGroups error: ${e.message}")
            emit(Result.failure(e))
        }

    }.toCommonFlow()
}