package com.piotrkalin.havira.groupDish.domain.interactors

import com.piotrkalin.havira.core.data.remote.NotFoundException
import com.piotrkalin.havira.core.domain.model.DishPrep
import com.piotrkalin.havira.groupDish.data.FakeDishService
import com.piotrkalin.havira.groupDish.data.model.CreateDishRequest
import com.piotrkalin.havira.groupDish.domain.IDishService
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class GetGroupDishByIdTest {

    private lateinit var fakeDishService: IDishService
    private lateinit var getGroupDishById: GetGroupDishById

    @Before
    fun setUp() {
        fakeDishService = FakeDishService()
        getGroupDishById = GetGroupDishById(fakeDishService)
    }

    @Test
    fun `get dish by id successfully`() = runBlocking {
        // Given a dish created in the fakeDishService
        val createDishRequest = CreateDishRequest(groupId = 1, title = "Test Dish", desc = "Test Dish Description")
        val createdDish = fakeDishService.createGroupDish(createDishRequest)

        // When invoking GetGroupDishById with the dish ID
        val result = getGroupDishById(createdDish.id!!)

        // Then the dish should be retrieved successfully
        assertTrue(result.isSuccess)
        val retrievedDish = result.getOrNull()
        assertNotNull(retrievedDish)
        assertEquals(createdDish.id, retrievedDish?.id)
        assertEquals(createdDish.title, retrievedDish?.title)
        assertEquals(createdDish.desc, retrievedDish?.desc)
    }

    @Test
    fun `get dish by id with error`() = runBlocking {
        // Given a non-existent dish ID
        val nonExistentDishId = -1L

        // When invoking GetGroupDishById with the non-existent dish ID
        val result = getGroupDishById(nonExistentDishId)

        // Then the result should be a failure
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertNotNull(exception)
        assertTrue(exception is NotFoundException) // Or any specific exception you expect in this case
    }

    @Test
    fun `get dish by id with sorted dish preps`() = runBlocking {
        // Given a dish with multiple dish preps
        val createDishRequest = CreateDishRequest(groupId = 1, title = "Test Dish", desc = "Test Dish Description")
        val createdDish = fakeDishService.createGroupDish(createDishRequest)

        val dishPrep1 = DishPrep(1, null, dishId = createdDish.id!!, rating = 4, date = System.currentTimeMillis() - 10000)
        val dishPrep2 = DishPrep(2, null, dishId = createdDish.id!!, rating = 5, date = System.currentTimeMillis() - 5000)
        val dishPrep3 = DishPrep(3, null, dishId = createdDish.id!!, rating = 3, date = System.currentTimeMillis())

        fakeDishService.addGroupDishPrep(dishPrep1.toAddDishPrepRequest(), createdDish.id!!)
        fakeDishService.addGroupDishPrep(dishPrep2.toAddDishPrepRequest(), createdDish.id!!)
        fakeDishService.addGroupDishPrep(dishPrep3.toAddDishPrepRequest(), createdDish.id!!)

        // When invoking GetGroupDishById with the dish ID
        val result = getGroupDishById(createdDish.id!!)

        // Then the dishPreps should be sorted in descending order by date
        assertTrue(result.isSuccess)
        val retrievedDish = result.getOrNull()
        assertNotNull(retrievedDish)
        val sortedDishPreps = retrievedDish?.dishPreps
        assertNotNull(sortedDishPreps)
        assertEquals(3, sortedDishPreps?.size)
        assertEquals(dishPrep3.id, sortedDishPreps?.get(0)?.id)
        assertEquals(dishPrep2.id, sortedDishPreps?.get(1)?.id)
        assertEquals(dishPrep1.id, sortedDishPreps?.get(2)?.id)
    }
}