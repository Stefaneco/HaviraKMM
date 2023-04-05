package com.piotrkalin.havira.auth.domain.interactors

import com.piotrkalin.havira.auth.data.FakeProfileService
import com.piotrkalin.havira.auth.data.FakeTokenDataSource
import com.piotrkalin.havira.core.domain.util.FakeImageFile
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CreateUserProfileTest {

    private lateinit var fakeProfileService: FakeProfileService
    private lateinit var fakeTokenDataSource: FakeTokenDataSource
    private lateinit var createUserProfile: CreateUserProfile

    @Before
    fun setUp() {
        fakeProfileService = FakeProfileService()
        fakeTokenDataSource = FakeTokenDataSource()
        createUserProfile = CreateUserProfile(fakeProfileService, fakeTokenDataSource)
    }

    @Test
    fun `create user profile with image successfully`() = runBlocking {
        // Given a name and an ImageFile
        val name = "Test User"
        val fakeImageFile = FakeImageFile()

        // When creating a user profile
        val result = createUserProfile(name, fakeImageFile)

        // Then the profile should be created successfully
        assertTrue(result.isSuccess)
    }

    @Test
    fun `create user profile without image successfully`() = runBlocking {
        // Given a name
        val name = "Test User"

        // When creating a user profile
        val result = createUserProfile(name, null)

        // Then the profile should be created successfully
        assertTrue(result.isSuccess)
    }

    @Test
    fun `create user profile with error from profile service`() = runBlocking {
        // Given a name
        val name = "Test User"

        // Make profileService throw an exception
        fakeProfileService.throwsError = true

        // When creating a user profile
        val result = createUserProfile(name, null)

        // Then the result should be a failure
        assertTrue(result.isFailure)
        assertEquals(fakeProfileService.errorMessage, result.exceptionOrNull()?.message)
    }
}