package com.piotrkalin.havira.group.data

import com.piotrkalin.havira.database.HaviraDatabase
import com.piotrkalin.havira.group.domain.IGroupDataSource
import com.piotrkalin.havira.group.domain.model.Group

class SqlDelightGroupDataSource(
    private val db : HaviraDatabase
) : IGroupDataSource {

    private val groupQueries = db.groupQueries

    override suspend fun getAllGroups(): List<Group> {
        return groupQueries.getAllGroups().executeAsList().map { it.toGroup() }
    }

    override suspend fun deleteGroupById(id: Long) {
        groupQueries.deleteGroupById(id)
    }

    override suspend fun insertGroup(group: Group) {
        groupQueries.insertGroup(group.toGroupEntity())
    }

    override suspend fun insertGroups(groups: List<Group>) {
        groupQueries.transaction {
            for (group in groups){
                groupQueries.insertGroup(group.toGroupEntity())
            }
        }
    }

    override suspend fun deleteGroupsByIds(ids: List<Long>) {
        groupQueries.transaction {
            for (id in ids){
                groupQueries.deleteGroupById(id)
            }
        }
    }
}