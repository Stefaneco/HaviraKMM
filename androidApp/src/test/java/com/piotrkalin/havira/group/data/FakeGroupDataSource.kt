package com.piotrkalin.havira.group.data

import com.piotrkalin.havira.group.domain.IGroupDataSource
import com.piotrkalin.havira.group.domain.model.Group
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class FakeGroupDataSource : IGroupDataSource {

    private val groups = mutableMapOf<Long, Group>()
    private val mutex = Mutex()

    override suspend fun getAllGroups(): List<Group> {
        return mutex.withLock {
            groups.values.toList()
        }
    }

    override suspend fun deleteGroupById(id: Long) {
        mutex.withLock {
            groups.remove(id)
        }
    }

    override suspend fun insertGroup(group: Group) {
        mutex.withLock {
            groups[group.id] = group
        }
    }

    override suspend fun insertGroups(groups: List<Group>) {
        mutex.withLock {
            this.groups.putAll(groups.associateBy { it.id })
        }
    }

    override suspend fun deleteGroupsByIds(ids: List<Long>) {
        mutex.withLock {
            ids.forEach { id -> groups.remove(id) }
        }
    }
}
