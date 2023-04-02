package com.piotrkalin.havira.core.domain.model

import com.piotrkalin.havira.groupDish.data.model.AddDishPrepRequest
import com.piotrkalin.havira.groupDish.data.model.DishPrepDto

data class DishPrep(
    val id: Long? = null,
    val userId : String? = null,
    val dishId: Long,
    val rating: Int,
    val date: Long
){
    companion object {
        fun fromDishPrepResponse(response: DishPrepDto) : DishPrep {
            return DishPrep(response.id, response.userId, response.dishId, response.rating, response.dateTimestamp)
        }
    }

    fun toAddDishPrepRequest() : AddDishPrepRequest {
        return AddDishPrepRequest(rating)
    }
}
