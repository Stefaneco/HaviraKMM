package com.piotrkalin.havira.android.di

import android.app.Application
import android.content.Context
import com.piotrkalin.havira.android.auth.data.TokenDataSource
import com.piotrkalin.havira.auth.data.AzureAuthService
import com.piotrkalin.havira.auth.domain.IAuthService
import com.piotrkalin.havira.auth.domain.ITokenDataSource
import com.piotrkalin.havira.auth.domain.interactors.AuthInteractors
import com.piotrkalin.havira.auth.domain.interactors.LoginWithGoogle
import com.piotrkalin.havira.auth.domain.interactors.Logout
import com.piotrkalin.havira.core.data.local.DatabaseDriverFactory
import com.piotrkalin.havira.core.data.remote.KtorClientFactory
import com.piotrkalin.havira.database.HaviraDatabase
import com.piotrkalin.havira.dish.data.SqlDelightDishDataSource
import com.piotrkalin.havira.dish.domain.IDishDataSource
import com.piotrkalin.havira.dish.domain.IDishRepository
import com.piotrkalin.havira.dish.domain.interactors.*
import com.piotrkalin.havira.group.data.AzureGroupService
import com.piotrkalin.havira.group.domain.IGroupService
import com.piotrkalin.havira.group.domain.interactors.CreateGroup
import com.piotrkalin.havira.group.domain.interactors.GroupInteractors
import com.squareup.sqldelight.db.SqlDriver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideGroupInteractors(groupService :IGroupService) : GroupInteractors {
        return GroupInteractors(
            CreateGroup(groupService)
        )
    }

    @Provides
    @Singleton
    fun provideGroupService(httpClient: HttpClient) : IGroupService {
        return AzureGroupService(httpClient)
    }

    @Provides
    @Singleton
    fun provideAuthInteractors(authService: IAuthService, tokenDataSource: ITokenDataSource) : AuthInteractors {
        return AuthInteractors(
            LoginWithGoogle(authService, tokenDataSource),
            Logout(authService, tokenDataSource)
        )
    }

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
        return com.piotrkalin.havira.dish.domain.DishRepository(localDishDataSource)
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

    @Provides
    @Singleton
    fun provideAuthService(httpClient: HttpClient) : IAuthService {
        return AzureAuthService(httpClient)
    }

    @Provides
    @Singleton
    fun provideHttpClient(tokenDataSource: ITokenDataSource) : HttpClient {
        return KtorClientFactory(CIO.create(), tokenDataSource).build()
    }

    @Provides
    @Singleton
    fun provideTokenDataSource(@ApplicationContext context: Context) : ITokenDataSource {
        return TokenDataSource(context)
    }

}