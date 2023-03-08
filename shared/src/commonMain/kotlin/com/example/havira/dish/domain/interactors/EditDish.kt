package com.example.havira.dish.domain.interactors

import com.example.havira.dish.domain.IDishRepository

class EditDish(
    private val dishRepository: IDishRepository
) {
    suspend operator fun invoke(id: Long, title: String, description: String) : Result<Unit?>{
        return try {
            dishRepository.updateDish(id, title, description)
            Result.success(null)
        } catch (e: Exception){
            Result.failure(e)
        }
    }
}