package com.piotrkalin.havira.group.domain

import com.piotrkalin.havira.group.domain.model.Group

interface IGroupDataSource {

    suspend fun getAllGroups() : List<Group>
    suspend fun deleteGroupById(id: Long)
    suspend fun insertGroup(group: Group)
    suspend fun insertGroups(groups : List<Group>)
    suspend fun deleteGroupsByIds(ids : List<Long>)
}