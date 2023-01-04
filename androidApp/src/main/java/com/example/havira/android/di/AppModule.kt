package com.example.havira.android.di

import android.app.Application
import com.example.havira.core.data.local.DatabaseDriverFactory
import com.example.havira.database.HaviraDatabase
import com.example.havira.dish.data.local.SqlDelightDishDataSource
import com.example.havira.dish.domain.DishRepository
import com.example.havira.dish.domain.IDishDataSource
import com.example.havira.dish.domain.IDishRepository
import com.example.havira.dish.domain.interactors.*
import com.example.havira.dish.interactors.DishInteractors
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
            AddDishPrep(dishRepository)
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