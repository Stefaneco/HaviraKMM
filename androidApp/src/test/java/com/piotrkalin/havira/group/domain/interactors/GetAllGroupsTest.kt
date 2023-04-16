package com.piotrkalin.havira.group.domain.interactors

import app.cash.turbine.test
import com.piotrkalin.havira.core.domain.util.DateTimeUtil
import com.piotrkalin.havira.group.data.FakeGroupDataSource
import com.piotrkalin.havira.group.data.FakeGroupService
import com.piotrkalin.havira.group.data.model.CreateGroupRequest
import com.piotrkalin.havira.group.domain.model.Group
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetAllGroupsTest {

    private lateinit var fakeGroupService : FakeGroupService
    private lateinit var fakeGroupDataSource : FakeGroupDataSource
    private lateinit var getAllGroups : GetAllGroups

    @Before
    fun setUp(){
        fakeGroupService = FakeGroupService()
        fakeGroupDataSource = FakeGroupDataSource()
        getAllGroups = GetAllGroups(fakeGroupService, fakeGroupDataSource)
    }

    @Test
    fun `get groups with no cache data and remote data`() = runBlocking {
        // Create groups in remote
        val group1 = fakeGroupService.createGroup(CreateGroupRequest("Group 1"))
        val group2 = fakeGroupService.createGroup(CreateGroupRequest("Group 2"))
        val groups = listOf(Group.fromGroupResponse(group1),Group.fromGroupResponse(group2))

        getAllGroups.invoke().test {
            // emits empty list from cache
            assertEquals(Result.success(listOf<Group>()), awaitItem())
            // emits list from remote
            assertEquals(Result.success(groups), awaitItem())
            awaitComplete()
        }

        // local cache is updated
        val cachedGroups = fakeGroupDataSource.getAllGroups()
        assertEquals(groups, cachedGroups)
    }

    @Test
    fun `get groups with deleted group in cache and remote data`() = runBlocking {
        // Create groups in remote
        val remoteGroup1 = fakeGroupService.createGroup(CreateGroupRequest("Remote Group 1"))
        val remoteGroup2 = fakeGroupService.createGroup(CreateGroupRequest("Remote Group 2"))
        val remoteGroups = listOf(Group.fromGroupResponse(remoteGroup1), Group.fromGroupResponse(remoteGroup2))

        // Add a group to cache
        val localGroup = Group(ownerId = "fakeUser", id = 999L, joinCode = "joinCode3", name = "Local Group", created = DateTimeUtil.now())
        fakeGroupDataSource.insertGroup(localGroup)

        getAllGroups.invoke().test {
            // Emits list from cache
            assertEquals(Result.success(listOf(localGroup)), awaitItem())
            // Emits list from remote
            assertEquals(Result.success(remoteGroups), awaitItem())
            awaitComplete()
        }

        // local cache is updated
        val cachedGroups = fakeGroupDataSource.getAllGroups()
        assertEquals(remoteGroups, cachedGroups)
    }

    @Test
    fun `get groups with group in cache and remote data`() = runBlocking {
        val localGroup = Group(ownerId = "fakeUser", id = 999L, joinCode = "joinCode3", name = "Local Group", created = DateTimeUtil.now())
        fakeGroupDataSource.insertGroup(localGroup)
        fakeGroupService.throwsError = true

        getAllGroups.invoke().test {
            // Emits list from cache
            assertEquals(Result.success(listOf(localGroup)), awaitItem())
            // Emits error
            val errorResult = awaitItem()
            assertTrue(errorResult.isFailure)
            assertEquals(fakeGroupService.errorMessage,errorResult.exceptionOrNull()?.message)
            awaitComplete()
        }
    }

    @Test
    fun `get groups with group in cache and remote data error`() = runBlocking {
        // Create groups in remote
        val remoteGroup1 = fakeGroupService.createGroup(CreateGroupRequest("Remote Group 1"))
        val remoteGroup2 = fakeGroupService.createGroup(CreateGroupRequest("Remote Group 2"))
        val remoteGroups = listOf(Group.fromGroupResponse(remoteGroup1), Group.fromGroupResponse(remoteGroup2))

        // Add a group to cache
        val localGroup = Group.fromGroupResponse(remoteGroup1)
        fakeGroupDataSource.insertGroup(localGroup)

        getAllGroups.invoke().test {
            // Emits list from cache
            assertEquals(Result.success(listOf(localGroup)), awaitItem())
            // Emits list from remote
            assertEquals(Result.success(remoteGroups), awaitItem())
            awaitComplete()
        }

        // local cache is updated
        val cachedGroups = fakeGroupDataSource.getAllGroups()
        assertEquals(remoteGroups, cachedGroups)
    }
}