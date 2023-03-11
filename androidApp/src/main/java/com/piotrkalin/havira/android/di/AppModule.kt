package com.piotrkalin.havira.android.di

import android.app.Application
import com.piotrkalin.havira.core.data.local.DatabaseDriverFactory
import com.piotrkalin.havira.database.HaviraDatabase
import com.piotrkalin.havira.dish.data.local.SqlDelightDishDataSource
import com.piotrkalin.havira.dish.domain.DishRepository
import com.piotrkalin.havira.dish.domain.IDishDataSource
import com.piotrkalin.havira.dish.domain.IDishRepository
import com.piotrkalin.havira.dish.domain.interactors.*
import com.piotrkalin.havira.dish.domain.interactors.DishInteractors
import com.squareup.sqldelight.db.SqlDriver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideDishInteractors(dishRepository: IDishRepository) : DishInteractors {
        return DishInteractors(
            AddDish(dishRepository),
            DeleteDishById(dishRepository),
            GetAllDishes(dishRepository),
            GetDishById(dishRepository),
            AddDishPrep(dishRepository),
            EditDish(dishRepository)
        )
    }

    @Provides
    @Singleton
    fun provideDishRepository(localDishDataSource: IDishDataSource) : IDishRepository {
        return DishRepository(localDishDataSource)
    }

    @Provides
    @Singleton
    fun provideLocalDishDataSource(driver : SqlDriver) : IDishDataSource {
        return SqlDelightDishDataSource(HaviraDatabase(driver))
    }

    @Provides
    @Singleton
    fun provideDatabaseDriver(app: Application): SqlDriver {
        return DatabaseDriverFactory(app).create()
    }
}