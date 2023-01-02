package com.example.havira.di

import com.example.havira.core.data.local.DatabaseDriverFactory
import com.example.havira.database.HaviraDatabase
import com.example.havira.dish.data.local.SqlDelightDishDataSource
import com.example.havira.dish.domain.DishRepository
import com.example.havira.dish.domain.IDishDataSource
import com.example.havira.dish.domain.IDishRepository
import com.example.havira.dish.domain.interactors.AddDish
import com.example.havira.dish.domain.interactors.DeleteDishById
import com.example.havira.dish.domain.interactors.GetAllDishes
import com.example.havira.dish.domain.interactors.GetDishById
import com.example.havira.dish.interactors.DishInteractors

interface IAppModule{
    val dishInteractors : DishInteractors
}

class AppModule : IAppModule {

    override val dishInteractors: DishInteractors by lazy {
        DishInteractors(
            AddDish(dishRepository),
            DeleteDishById(dishRepository),
            GetAllDishes(dishRepository),
            GetDishById(dishRepository)
        )
    }

    private val dishRepository : IDishRepository by lazy {
        DishRepository(localDishDataSource)
    }

    private val localDishDataSource : IDishDataSource by lazy {
        SqlDelightDishDataSource(
            HaviraDatabase(
                DatabaseDriverFactory().create()
            )
        )
    }
}