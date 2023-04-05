package com.piotrkalin.havira.groupDish.domain.interactors

import com.piotrkalin.havira.core.data.remote.NotFoundException
import com.piotrkalin.havira.groupDish.data.FakeDishService
import com.piotrkalin.havira.groupDish.data.model.CreateDishRequest
import com.piotrkalin.havira.groupDish.domain.IDishService
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class CreateGroupDishTest {
    private lateinit var fakeDishService: IDishService
    private lateinit var createGroupDish: CreateGroupDish

    @Before
    fun setUp() {
        fakeDishService = FakeDishService()
        createGroupDish = CreateGroupDish(fakeDishService)
    }

    @Test
    fun `create dish successfully`() = runBlocking {
        // Given a CreateDishRequest
        val createDishRequest = CreateDishRequest(groupId = 1, title = "Test Dish", desc = "Test Dish Description")

        // When invoking CreateGroupDish with the request
        val result = createGroupDish(createDishRequest)

        // Then a new dish should be created successfully
        assertTrue(result.isSuccess)
        val createdDish = result.getOrNull()
        assertNotNull(createdDish)
        assertEquals(createDishRequest.title, createdDish?.title)
        assertEquals(createDishRequest.desc, createdDish?.desc)
    }

    @Test
    fun `create dish with error`() = runBlocking {
        // Given a CreateDishRequest that causes an error
        val createDishRequest = CreateDishRequest(groupId = -1, title = "Error Dish", desc = "Error Dish Description")

        // When invoking CreateGroupDish with the request
        val result = createGroupDish(createDishRequest)

        // Then the result should be a failure
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertNotNull(exception)
        assertTrue(exception is NotFoundException)
    }
}