package com.piotrkalin.havira.di

import com.piotrkalin.havira.core.data.local.DatabaseDriverFactory
import com.piotrkalin.havira.database.HaviraDatabase
import com.piotrkalin.havira.dish.data.local.SqlDelightDishDataSource
import com.piotrkalin.havira.dish.domain.DishRepository
import com.piotrkalin.havira.dish.domain.IDishDataSource
import com.piotrkalin.havira.dish.domain.IDishRepository
import com.piotrkalin.havira.dish.domain.interactors.*

interface IAppModule{
    val dishInteractors : DishInteractors
}

class AppModule : IAppModule {

    override val dishInteractors: DishInteractors by lazy {
        DishInteractors(
            AddDish(dishRepository),
            DeleteDishById(dishRepository),
            GetAllDishes(dishRepository),
            GetDishById(dishRepository),
            AddDishPrep(dishRepository),
            EditDish(dishRepository)
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