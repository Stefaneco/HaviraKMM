package com.example.havira.dish.domain.interactors

import com.example.havira.dish.domain.IDishRepository

class DeleteDishById(
    private val dishRepository: IDishRepository
) {
    suspend operator fun invoke(id: Long){
        dishRepository.deleteDishById(id)
    }
}