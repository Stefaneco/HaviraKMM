package com.piotrkalin.havira.auth.domain.interactors

import com.piotrkalin.havira.auth.data.FakeProfileService
import com.piotrkalin.havira.auth.data.FakeTokenDataSource
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class IsUserProfileCreatedTest {

    private lateinit var fakeProfileService: FakeProfileService
    private lateinit var fakeTokenDataSource: FakeTokenDataSource
    private lateinit var isUserProfileCreated: IsUserProfileCreated

    @Before
    fun setUp() {
        fakeProfileService = FakeProfileService()
        fakeTokenDataSource = FakeTokenDataSource()
        isUserProfileCreated = IsUserProfileCreated(fakeProfileService, fakeTokenDataSource)
    }

    @Test
    fun `isUserProfileCreated returns true when found on device`() = runBlocking {
        // Given a profileId saved on the device
        fakeTokenDataSource.updateProfileId("fake_user_id")

        // When checking if the profile is created
        val result = isUserProfileCreated()

        // Then the result should be successful and true
        assertTrue(result.isSuccess && result.getOrNull() == true)
    }

    @Test
    fun `isUserProfileCreated returns true when found remotely`() = runBlocking {
        // Given a profile saved in the remote service
        fakeProfileService.createUserProfile("Fake User", null)

        // When checking if the profile is created
        val result = isUserProfileCreated()

        // Then the result should be successful and true
        assertTrue(result.isSuccess && result.getOrNull() == true)

        // And the profile should be saved on the device
        assertNotNull(fakeTokenDataSource.getProfileId())
    }

    @Test
    fun `isUserProfileCreated returns false when not found on device or remotely`() = runBlocking {
        // Given no profile saved on the device or in the remote service

        // When checking if the profile is created
        val result = isUserProfileCreated()

        // Then the result should be successful and false
        assertTrue(result.isSuccess && result.getOrNull() == false)
    }

    @Test
    fun `isUserProfileCreated returns failure when an exception occurs`() = runBlocking {
        // Given an exception occurs during the remote call
        fakeProfileService.throwsError = true
        fakeProfileService.errorMessage = "Profile service error"

        // When checking if the profile is created
        val result = isUserProfileCreated()

        // Then the result should be a failure with the given exception
        assertTrue(result.isFailure && (result.exceptionOrNull() as? RuntimeException)?.message == "Profile service error")
    }
}
