package com.piotrkalin.havira.android.auth.presentation

import android.content.Context
import android.net.Uri
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.piotrkalin.havira.android.R
import com.piotrkalin.havira.auth.presentation.LoginEvent
import com.piotrkalin.havira.auth.presentation.LoginScreenState
import com.piotrkalin.havira.auth.presentation.LoginStage
import com.piotrkalin.havira.core.domain.util.ImageFile
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class LoginScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var events : MutableList<LoginEvent>
    private val onEvent: (LoginEvent) -> Unit = { event ->
        events.add(event)
    }

    @Before
    fun setUp(){
        events = mutableListOf()
    }

    @Test
    fun testLoginScreen_StageStateWaitingForProviderSelection() = runTest {
        val state = LoginScreenState(
            stage = LoginStage.WAITING_FOR_PROVIDER_SELECTION
        )

        composeTestRule.setContent {
            LoginScreen(state, onEvent)
        }

        // Test if the correct child composable is shown based on the stage
        composeTestRule.onNodeWithTag("ProviderSelection").assertExists()
        composeTestRule.onNodeWithTag("LoadingScreen").assertDoesNotExist()
        composeTestRule.onNodeWithTag("LoginProfileCreation").assertDoesNotExist()
    }

    @Test
    fun testLoginScreen_StageStateConnectingToAzure() = runTest {
        val state = LoginScreenState(
            stage = LoginStage.CONNECTING_TO_AZURE
        )

        composeTestRule.setContent {
            LoginScreen(state, onEvent)
        }

        // Test if the correct child composable is shown based on the stage
        composeTestRule.onNodeWithTag("ProviderSelection").assertDoesNotExist()
        composeTestRule.onNodeWithTag("LoadingScreen").assertExists()
        composeTestRule.onNodeWithTag("LoginProfileCreation").assertDoesNotExist()
    }

    @Test
    fun testLoginScreen_StageStateLoadingProfileInfo() = runTest {
        val state = LoginScreenState(
            stage = LoginStage.LOADING_PROFILE_INFO
        )

        composeTestRule.setContent {
            LoginScreen(state, onEvent)
        }

        // Test if the correct child composable is shown based on the stage
        composeTestRule.onNodeWithTag("ProviderSelection").assertDoesNotExist()
        composeTestRule.onNodeWithTag("LoadingScreen").assertExists()
        composeTestRule.onNodeWithTag("LoginProfileCreation").assertDoesNotExist()
    }

    @Test
    fun testLoginScreen_StageStateWaitingForProfileCreation() = runTest {
        val state = LoginScreenState(
            stage = LoginStage.WAITING_FOR_PROFILE_CREATION
        )

        composeTestRule.setContent {
            LoginScreen(state, onEvent)
        }

        // Test if the correct child composable is shown based on the stage
        composeTestRule.onNodeWithTag("ProviderSelection").assertDoesNotExist()
        composeTestRule.onNodeWithTag("LoadingScreen").assertDoesNotExist()
        composeTestRule.onNodeWithTag("LoginProfileCreation").assertExists()
    }

    @Test
    fun testLoginScreen_StageStateCreatingProfile() = runTest {
        val state = LoginScreenState(
            stage = LoginStage.CREATING_PROFILE
        )

        composeTestRule.setContent {
            LoginScreen(state, onEvent)
        }

        // Test if the correct child composable is shown based on the stage
        composeTestRule.onNodeWithTag("ProviderSelection").assertDoesNotExist()
        composeTestRule.onNodeWithTag("LoadingScreen").assertExists()
        composeTestRule.onNodeWithTag("LoginProfileCreation").assertDoesNotExist()
    }

    @Test
    fun testLoginScreen_ErrorSnackbar() = runTest {
        val state = LoginScreenState(
            error = "Test error message"
        )

        composeTestRule.setContent {
            LoginScreen(state, onEvent)
        }

        // Test if the snackbar is shown with the correct error message
        composeTestRule.onNodeWithText("Test error message").assertExists()

        // Test if the OnErrorSeen event is dispatched
        assertTrue(events.contains(LoginEvent.OnErrorSeen))
    }

    @Test
    fun testLoginProfileCreation_UIComponentsDisplayed() = runTest {
        val state = LoginScreenState(
            stage = LoginStage.WAITING_FOR_PROFILE_CREATION
        )

        composeTestRule.setContent {
            LoginProfileCreation(state, onEvent)
        }

        // Test if the AsyncImage, TextField and Save Button are displayed
        composeTestRule.onNodeWithTag("ProfileImage").assertExists()
        composeTestRule.onNodeWithTag("NameTextField").assertExists()
        composeTestRule.onNodeWithTag("SaveProfileButton").assertExists()
    }

    @Test
    fun testLoginProfileCreation_DefaultImageDisplayed() = runTest {
        val state = LoginScreenState(
            stage = LoginStage.WAITING_FOR_PROFILE_CREATION
        )

        composeTestRule.setContent {
            LoginProfileCreation(state, onEvent)
        }

        val context: Context = InstrumentationRegistry.getInstrumentation().targetContext

        // Test if the default image is displayed when no image is selected
        composeTestRule.onNodeWithTag("ProfileImage").assertExists()
        composeTestRule.onNodeWithTag("ProfileImage").assertContentDescriptionEquals(
            context.getString(R.string.default_image_description)
        )
    }

    @Test
    fun testLoginProfileCreation_SelectedImageDisplayed() = runTest {
        val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
        val state = LoginScreenState(
            stage = LoginStage.WAITING_FOR_PROFILE_CREATION,
            image = ImageFile(uri = Uri.parse("fake_uri"), contentResolver = context.contentResolver)
        )

        composeTestRule.setContent {
            LoginProfileCreation(state, onEvent)
        }

        // Test if the selected image is displayed when image is selected
        composeTestRule.onNodeWithTag("ProfileImage").assertExists()
        composeTestRule.onNodeWithTag("ProfileImage").assertContentDescriptionEquals(
            context.getString(R.string.selected_image_description)
        )
    }

    @Test
    fun testLoginProfileCreation_SaveButtonWhenValidProfile() = runTest {
        val state = LoginScreenState(
            stage = LoginStage.WAITING_FOR_PROFILE_CREATION,
            isValidProfile = true
        )

        composeTestRule.setContent {
            LoginProfileCreation(state, onEvent)
        }

        // Test if the save button is enabled when profile is valid
        composeTestRule.onNodeWithTag("SaveProfileButton").assertExists()
        composeTestRule.onNodeWithTag("SaveProfileButton").assertIsEnabled()

        // Test if the SaveProfile event is dispatched
        composeTestRule.onNodeWithTag("SaveProfileButton").performClick()
        assertTrue(events.contains(LoginEvent.SaveProfile()))
    }

    @Test
    fun testLoginProfileCreation_SaveButtonWhenInvalidProfile() = runTest {
        val state = LoginScreenState(
            stage = LoginStage.WAITING_FOR_PROFILE_CREATION,
            isValidProfile = false
        )

        composeTestRule.setContent {
            LoginProfileCreation(state, onEvent)
        }

        // Test if the save button is enabled when profile is valid
        composeTestRule.onNodeWithTag("SaveProfileButton").assertExists()
        composeTestRule.onNodeWithTag("SaveProfileButton").assertIsNotEnabled()
    }

    @Test
    fun testLoginProfileCreation_NameTextFieldDisplaysName() = runTest {
        val name = "Test Name"
        val state = LoginScreenState(
            stage = LoginStage.WAITING_FOR_PROFILE_CREATION,
            name = name
        )

        composeTestRule.setContent {
            LoginProfileCreation(state, onEvent)
        }

        // Test if the save button is enabled when profile is valid
        composeTestRule.onNodeWithTag("NameTextField").assertExists()
        composeTestRule.onNodeWithTag("NameTextField").assertTextEquals(name)
    }
}