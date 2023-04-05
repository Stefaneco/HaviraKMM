package com.piotrkalin.havira.groupDish.data

import com.piotrkalin.havira.core.data.remote.NotFoundException
import com.piotrkalin.havira.groupDish.data.model.AddDishPrepRequest
import com.piotrkalin.havira.groupDish.data.model.CreateDishRequest
import com.piotrkalin.havira.groupDish.data.model.DishPrepDto
import com.piotrkalin.havira.groupDish.data.model.DishResponse
import com.piotrkalin.havira.groupDish.domain.IDishService
import kotlinx.coroutines.delay

class FakeDishService : IDishService {

    private var requestUserId = "fake_user"

    private val fakeDishes = mutableListOf<DishResponse>()
    private val fakeDishPreps = mutableMapOf<Long, MutableList<DishPrepDto>>()

    public fun setRequestUserId(id: String) {
        requestUserId = id
    }

    override suspend fun createGroupDish(request: CreateDishRequest): DishResponse {
        delay(500) // Simulate network delay
        if (request.groupId <= 0) throw NotFoundException()
        val newDish = DishResponse(
            id = (fakeDishes.size + 1).toLong(),
            ownerId = requestUserId,
            groupId = request.groupId.toInt(),
            title = request.title,
            desc = request.desc,
            rating = 0f,
            nofRatings = 0,
            lastMadeTimestamp = null,
            createdTimestamp = System.currentTimeMillis(),
            dishPreps = emptyList()
        )
        fakeDishes.add(newDish)
        return newDish
    }

    override suspend fun addGroupDishPrep(request: AddDishPrepRequest, dishId: Long): DishPrepDto {
        delay(500) // Simulate network delay
        val dishPreps = fakeDishPreps.getOrPut(dishId) { mutableListOf() }
        val newDishPrep = DishPrepDto(
            id = (dishPreps.size + 1).toLong(),
            userId = requestUserId,
            dishId = dishId,
            rating = request.rating,
            dateTimestamp = System.currentTimeMillis()
        )
        dishPreps.add(newDishPrep)

        // Update the DishResponse with the new DishPrepDto and ratings
        val dish = fakeDishes.find { it.id == dishId }
        if (dish != null) {
            dish.nofRatings += 1
            dish.rating = ((dish.rating * (dish.nofRatings - 1)) + request.rating) / dish.nofRatings
            dish.lastMadeTimestamp = newDishPrep.dateTimestamp
        }

        return newDishPrep
    }

    override suspend fun getGroupDishById(dishId: Long): DishResponse {
        delay(500) // Simulate network delay
        if (dishId <= 0) throw NotFoundException()
        val dishResponseWithoutDishPreps = fakeDishes.find { it.id == dishId }
            ?: throw NotFoundException()
        return dishResponseWithoutDishPreps.copy(
            dishPreps = fakeDishPreps.getOrPut(dishId) { mutableListOf() }
        )
    }
}