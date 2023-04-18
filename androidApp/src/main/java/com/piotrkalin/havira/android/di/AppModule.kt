package com.piotrkalin.havira.android.di

import android.app.Application
import android.content.Context
import com.piotrkalin.havira.android.auth.data.TokenDataSource
import com.piotrkalin.havira.auth.data.AzureAuthService
import com.piotrkalin.havira.auth.data.AzureProfileService
import com.piotrkalin.havira.auth.domain.IAuthService
import com.piotrkalin.havira.auth.domain.IProfileService
import com.piotrkalin.havira.auth.domain.ITokenDataSource
import com.piotrkalin.havira.auth.domain.interactors.*
import com.piotrkalin.havira.core.data.local.DatabaseDriverFactory
import com.piotrkalin.havira.core.data.remote.KtorClientFactory
import com.piotrkalin.havira.database.HaviraDatabase
import com.piotrkalin.havira.dish.data.SqlDelightDishDataSource
import com.piotrkalin.havira.dish.domain.IDishDataSource
import com.piotrkalin.havira.dish.domain.IDishRepository
import com.piotrkalin.havira.dish.domain.interactors.*
import com.piotrkalin.havira.group.data.AzureGroupService
import com.piotrkalin.havira.group.data.SqlDelightGroupDataSource
import com.piotrkalin.havira.group.domain.IGroupDataSource
import com.piotrkalin.havira.group.domain.IGroupService
import com.piotrkalin.havira.group.domain.interactors.*
import com.piotrkalin.havira.groupDish.data.AzureDishService
import com.piotrkalin.havira.groupDish.domain.IDishService
import com.piotrkalin.havira.groupDish.domain.interactors.AddGroupDishPrep
import com.piotrkalin.havira.groupDish.domain.interactors.CreateGroupDish
import com.piotrkalin.havira.groupDish.domain.interactors.GetGroup
import com.piotrkalin.havira.groupDish.domain.interactors.GetGroupDishById
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
    fun provideGroupInteractors(groupService :IGroupService, groupDataSource: IGroupDataSource) : GroupInteractors {
        return GroupInteractors(
            CreateGroup(groupService),
            GetAllGroups(groupService, groupDataSource),
            GetGroup(groupService),
            JoinGroup(groupService),
            LeaveGroup(groupService),
            DisbandGroup(groupService)
        )
    }

    @Provides
    @Singleton
    fun provideProfileService(httpClient: HttpClient) : IProfileService {
        return AzureProfileService(httpClient)
    }

    @Provides
    @Singleton
    fun provideDishService(httpClient: HttpClient) : IDishService {
        return AzureDishService(httpClient)
    }

    @Provides
    @Singleton
    fun provideGroupService(httpClient: HttpClient) : IGroupService {
        return AzureGroupService(httpClient)
    }

    @Provides
    @Singleton
    fun provideAuthInteractors(authService: IAuthService, tokenDataSource: ITokenDataSource, profileService: IProfileService) : AuthInteractors {
        return AuthInteractors(
            LoginWithGoogle(authService, profileService, tokenDataSource),
            Logout(authService, tokenDataSource),
            GetUserProfile(profileService),
            IsUserProfileCreated(profileService, tokenDataSource),
            CreateUserProfile(profileService, tokenDataSource),
            IsUserLoggedIn(tokenDataSource),
            GetUserProfileId(tokenDataSource)
        )
    }

    @Provides
    @Singleton
    fun provideDishInteractors(dishRepository: IDishRepository, dishService: IDishService) : DishInteractors {
        return DishInteractors(
            AddDish(dishRepository),
            DeleteDishById(dishRepository),
            GetAllDishes(dishRepository),
            GetDishById(dishRepository),
            AddDishPrep(dishRepository),
            EditDish(dishRepository),
            CreateGroupDish(dishService),
            AddGroupDishPrep(dishService),
            GetGroupDishById(dishService)
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
    fun provideLocalGroupDataSource(driver: SqlDriver) : IGroupDataSource {
        return SqlDelightGroupDataSource(HaviraDatabase(driver))
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
        //val mockApiEngine = MockApiEngine()
        //return KtorClientFactory(mockApiEngine.get(), tokenDataSource).build()
        return KtorClientFactory(CIO.create(), tokenDataSource).build()
    }

    @Provides
    @Singleton
    fun provideTokenDataSource(@ApplicationContext context: Context) : ITokenDataSource {
        return TokenDataSource(context)
    }

}