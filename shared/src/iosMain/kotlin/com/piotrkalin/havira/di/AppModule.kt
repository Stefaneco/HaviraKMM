package com.piotrkalin.havira.di

import com.piotrkalin.havira.core.data.local.DatabaseDriverFactory
import com.piotrkalin.havira.database.HaviraDatabase
import com.piotrkalin.havira.dish.data.SqlDelightDishDataSource

interface IAppModule{
    val dishInteractors : com.piotrkalin.havira.dish.domain.interactors.DishInteractors
}

class AppModule : IAppModule {

    override val dishInteractors: com.piotrkalin.havira.dish.domain.interactors.DishInteractors by lazy {
        com.piotrkalin.havira.dish.domain.interactors.DishInteractors(
            com.piotrkalin.havira.dish.domain.interactors.AddDish(dishRepository),
            com.piotrkalin.havira.dish.domain.interactors.DeleteDishById(dishRepository),
            com.piotrkalin.havira.dish.domain.interactors.GetAllDishes(dishRepository),
            com.piotrkalin.havira.dish.domain.interactors.GetDishById(dishRepository),
            com.piotrkalin.havira.dish.domain.interactors.AddDishPrep(dishRepository),
            com.piotrkalin.havira.dish.domain.interactors.EditDish(dishRepository)
        )
    }

    private val dishRepository : com.piotrkalin.havira.dish.domain.IDishRepository by lazy {
        com.piotrkalin.havira.dish.domain.DishRepository(localDishDataSource)
    }

    private val localDishDataSource : com.piotrkalin.havira.dish.domain.IDishDataSource by lazy {
        SqlDelightDishDataSource(
            HaviraDatabase(
                DatabaseDriverFactory().create()
            )
        )
    }
}