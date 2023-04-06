package com.piotrkalin.havira.auth.presentation

import com.piotrkalin.havira.auth.domain.interactors.*
import com.piotrkalin.havira.core.domain.util.ImageFile
import com.piotrkalin.havira.core.domain.util.toCommonFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class LoginViewModelTest {

    private lateinit var viewModel: LoginViewModel
    private val loginWithGoogle = mock<LoginWithGoogle>()
    private val logout = mock<Logout>()
    private val getUserProfile = mock<GetUserProfile>()
    private val isUserProfileCreated = mock<IsUserProfileCreated>()
    private val createUserProfile = mock<CreateUserProfile>()
    private val isUserLoggedIn = mock<IsUserLoggedIn>()
    private val authInteractors = AuthInteractors(
        loginWithGoogle = loginWithGoogle,
        logout = logout,
        getUserProfile = getUserProfile,
        isUserProfileCreated = isUserProfileCreated,
        createUserProfile = createUserProfile,
        isUserLoggedIn = isUserLoggedIn
    )
    private val testScope = TestScope()

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher(testScope.testScheduler))
        viewModel = LoginViewModel(authInteractors, testScope)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test Google Login - Connected to Azure`() = testScope.runTest {
        val idToken = "test-id-token"
        val connectedToAzureFlow = flowOf(Result.success(LoginWithGoogleState.CONNECTED_TO_AZURE)).toCommonFlow()

        whenever(authInteractors.loginWithGoogle(idToken)).thenReturn(connectedToAzureFlow)

        viewModel.onEvent(LoginEvent.GoogleLogin(idToken))

        assertEquals(LoginStage.CONNECTING_TO_AZURE, viewModel.state.value.stage)

        advanceUntilIdle()

        assertEquals(LoginStage.LOADING_PROFILE_INFO, viewModel.state.value.stage)
    }

    @Test
    fun `test Google Login - Profile Found`() = testScope.runTest { val idToken = "test-id-token"
        val profileFoundFlow = flowOf(Result.success(LoginWithGoogleState.PROFILE_FOUND)).toCommonFlow()
        whenever(authInteractors.loginWithGoogle(idToken)).thenReturn(profileFoundFlow)

        var onSuccessCalled = false
        viewModel.onEvent(LoginEvent.GoogleLogin(idToken) { onSuccessCalled = true })

        assertEquals(LoginStage.CONNECTING_TO_AZURE, viewModel.state.value.stage)

        advanceUntilIdle()

        assertEquals(true, onSuccessCalled) }

    @Test
    fun `test Google Login - Profile Not Found`() = testScope.runTest { val idToken = "test-id-token"
        val profileNotFoundFlow = flowOf(Result.success(LoginWithGoogleState.PROFILE_NOT_FOUND)).toCommonFlow()
        whenever(authInteractors.loginWithGoogle(idToken)).thenReturn(profileNotFoundFlow)

        viewModel.onEvent(LoginEvent.GoogleLogin(idToken))

        assertEquals(LoginStage.CONNECTING_TO_AZURE, viewModel.state.value.stage)

        advanceUntilIdle()

        assertEquals(LoginStage.WAITING_FOR_PROFILE_CREATION, viewModel.state.value.stage)
    }

    @Test
    fun `test Google Login - Error`() = testScope.runTest { val idToken = "test-id-token"
        val errorMessage = "Unknown error"
        val errorFlow = flowOf(Result.failure<LoginWithGoogleState>(Exception(errorMessage))).toCommonFlow()
        whenever(authInteractors.loginWithGoogle(idToken)).thenReturn(errorFlow)

        viewModel.onEvent(LoginEvent.GoogleLogin(idToken))

        assertEquals(LoginStage.CONNECTING_TO_AZURE, viewModel.state.value.stage)

        advanceUntilIdle()

        assertEquals(LoginStage.WAITING_FOR_PROVIDER_SELECTION, viewModel.state.value.stage)
        assertEquals(errorMessage, viewModel.state.value.error)
    }

    @Test
    fun `test OnErrorSeen - clears error`() {
        viewModel.onEvent(LoginEvent.SetError("Test error"))
        viewModel.onEvent(LoginEvent.OnErrorSeen)

        assertNull(viewModel.state.value.error)
    }

    @Test
    fun `test OnImageSelected - updates image`() = testScope.runTest {
        val testImage = mock<ImageFile>()

        viewModel.onEvent(LoginEvent.OnImageSelected(testImage))

        assertEquals(testImage, viewModel.state.value.image)
    }

    @Test
    fun `test EditName - updates name and sets isValidProfile to true for valid name`() {
        val newName = "Test User"
        viewModel.onEvent(LoginEvent.EditName(newName))

        val currentState = viewModel.state.value
        assertEquals(newName, currentState.name)
        assertTrue(currentState.isValidProfile)
    }

    @Test
    fun `test EditName - updates name and sets isValidProfile to false for invalid name`() {
        val invalidName = "N "
        viewModel.onEvent(LoginEvent.EditName(invalidName))

        val currentState = viewModel.state.value
        assertEquals(invalidName, currentState.name)
        assertFalse(currentState.isValidProfile)
    }

    @Test
    fun `test SaveProfile - calls event onSuccess on result success`() = testScope.runTest {
        val name = "Test User"
        var onSuccessCalled = false

        whenever(authInteractors.createUserProfile("Test User", null)).thenReturn(Result.success(null))

        viewModel.onEvent(LoginEvent.EditName(name))
        assertEquals(name, viewModel.state.value.name)

        viewModel.onEvent(LoginEvent.SaveProfile { onSuccessCalled = true } )

        advanceUntilIdle()
        assertTrue(onSuccessCalled)
    }

    @Test
    fun `test SaveProfile - updates error and sets stage to waiting for profile creation on failure`() = testScope.runTest {
        val name = "Test User"
        val error = "Unknown error"
        var onSuccessCalled = false

        whenever(authInteractors.createUserProfile("Test User", null)).thenReturn(Result.failure(Exception(error)))

        viewModel.onEvent(LoginEvent.EditName(name))
        assertEquals(name, viewModel.state.value.name)

        viewModel.onEvent(LoginEvent.SaveProfile { onSuccessCalled = true } )

        advanceUntilIdle()

        assertFalse(onSuccessCalled)
        assertEquals(error, viewModel.state.value.error)
        assertEquals(LoginStage.WAITING_FOR_PROFILE_CREATION, viewModel.state.value.stage)
    }

    @Test
    fun `test SetError - error is set`() {
        val viewModel = LoginViewModel(authInteractors, testScope)

        viewModel.onEvent(LoginEvent.SetError("Test error"))

        assertEquals("Test error", viewModel.state.value.error)
    }
}