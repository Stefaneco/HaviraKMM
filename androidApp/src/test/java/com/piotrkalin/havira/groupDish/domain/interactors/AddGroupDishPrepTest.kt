package com.piotrkalin.havira.groupDish.domain.interactors

import com.piotrkalin.havira.core.domain.model.Dish
import com.piotrkalin.havira.core.domain.model.DishPrep
import com.piotrkalin.havira.groupDish.data.FakeDishService
import com.piotrkalin.havira.groupDish.data.model.CreateDishRequest
import com.piotrkalin.havira.groupDish.domain.IDishService
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class AddGroupDishPrepTest {

    private lateinit var fakeDishService: IDishService
    private lateinit var addGroupDishPrep: AddGroupDishPrep

    @Before
    fun setUp() {
        fakeDishService = FakeDishService()
        addGroupDishPrep = AddGroupDishPrep(fakeDishService)
    }

    @Test
    fun `add dish prep successfully`() = runBlocking {
        // Given a dish and a dish prep
        val createDishRequest =
            CreateDishRequest(groupId = 1, title = "Test Dish", desc = "Test Dish Description")
        val dishResponse = fakeDishService.createGroupDish(createDishRequest)
        val dish = Dish.fromDishResponse(dishResponse)
        val dishPrep = DishPrep(null, null, dishId = dishResponse.id!!, rating = 4, date = System.currentTimeMillis())

        // When adding the dish prep
        val result = addGroupDishPrep(dishPrep, dish)

        // Then the dish should be updated with the new dish prep
        assertTrue(result.isSuccess)
        val updatedDish = result.getOrNull()
        assertNotNull(updatedDish)
        assertEquals(1, updatedDish?.dishPreps?.size)
        val addedDishPrep = updatedDish?.dishPreps?.first()
        assertNotNull(addedDishPrep)

        // Check if the dish rating and number of ratings are updated
        assertEquals(4f, updatedDish?.rating)
        assertEquals(1, updatedDish?.nofRatings)
    }

    @Test
    fun `add multiple dish preps and update ratings`() = runBlocking {
        // Given a dish and multiple dish preps
        val createDishRequest = CreateDishRequest(groupId = 1, title = "Test Dish", desc = "Test Dish Description")
        val dishResponse = fakeDishService.createGroupDish(createDishRequest)
        val dishPrep1 = DishPrep(null, null, dishId = dishResponse.id!!, rating = 4, date = System.currentTimeMillis())
        val dishPrep2 = DishPrep(null, null, dishId = dishResponse.id!!, rating = 5, date = System.currentTimeMillis())

        // When adding the dish preps
        val result1 = addGroupDishPrep(dishPrep1, Dish.fromDishResponse(dishResponse))
        val updatedDish1 = result1.getOrNull()
        val result2 = addGroupDishPrep(dishPrep2, updatedDish1!!)

        // Then the dish should be updated with the new dish preps and correct ratings
        assertTrue(result2.isSuccess)
        val updatedDish2 = result2.getOrNull()
        assertNotNull(updatedDish2)
        assertEquals(2, updatedDish2?.dishPreps?.size)

        // Check if the dish rating and number of ratings are updated
        assertEquals((4f + 5f) / 2, updatedDish2?.rating)
        assertEquals(2, updatedDish2?.nofRatings)
    }

    @Test
    fun `add dish prep with invalid rating`() = runBlocking {
        // Given a dish and a dish prep with an invalid rating
        val createDishRequest = CreateDishRequest(groupId = 1, title = "Test Dish", desc = "Test Dish Description")
        val dishResponse = fakeDishService.createGroupDish(createDishRequest)
        val dishPrep = DishPrep(1, "1", dishId = dishResponse.id!!, rating = 6, date = System.currentTimeMillis())

        // When adding the dish prep
        val result = addGroupDishPrep(dishPrep, Dish.fromDishResponse(dishResponse))

        // Then the result should be a failure
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertNotNull(exception)
        assertTrue(exception is IllegalArgumentException)
        assertEquals("Rating must be between 1 and 5", exception!!.message)
    }
}