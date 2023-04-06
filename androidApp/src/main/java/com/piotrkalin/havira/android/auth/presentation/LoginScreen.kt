package com.piotrkalin.havira.android.auth.presentation

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberAsyncImagePainter
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.piotrkalin.havira.android.BuildConfig
import com.piotrkalin.havira.android.R
import com.piotrkalin.havira.android.core.presentation.LoadingScreen
import com.piotrkalin.havira.android.findActivity
import com.piotrkalin.havira.auth.presentation.LoginEvent
import com.piotrkalin.havira.auth.presentation.LoginScreenState
import com.piotrkalin.havira.auth.presentation.LoginStage.*
import com.piotrkalin.havira.core.domain.util.ImageFile
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoilApi::class, ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    state: LoginScreenState,
    onEvent: (LoginEvent) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    state.error?.let {
        scope.launch {
            onEvent(LoginEvent.OnErrorSeen)
            snackbarHostState.showSnackbar(it)
        }
    }
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ){ paddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)){
            when(state.stage){
                //WAITING_FOR_PROVIDER_SELECTION -> LoginProfileCreation(state = state, onEvent = onEvent)
                WAITING_FOR_PROVIDER_SELECTION -> ProviderSelection(state = state, onEvent = onEvent)
                CONNECTING_TO_AZURE -> LoadingScreen()
                LOADING_PROFILE_INFO -> LoadingScreen()
                WAITING_FOR_PROFILE_CREATION -> LoginProfileCreation(state = state, onEvent = onEvent)
                CREATING_PROFILE -> LoadingScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@ExperimentalCoilApi
fun LoginProfileCreation(
    state: LoginScreenState,
    onEvent: (LoginEvent) -> Unit
){
    val context = LocalContext.current

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if(uri == null) return@rememberLauncherForActivityResult
            onEvent(LoginEvent.OnImageSelected(ImageFile(uri, context.contentResolver)))
        }
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag("LoginProfileCreation")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val defaultPictureUri = Uri.parse("android.resource://com.piotrkalin.havira.android/drawable/default_picture")
            Spacer(modifier = Modifier
                .heightIn(100.dp))
            val painter = rememberAsyncImagePainter(model = state.image?.uri ?: defaultPictureUri)
            Image(
                painter = painter,
                contentDescription =
                if (state.image?.uri == null) stringResource(id = R.string.default_image_description)
                else stringResource(id = R.string.selected_image_description),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.Gray, CircleShape)
                    .clickable {
                        singlePhotoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                    .testTag("ProfileImage"),
            )
            Spacer(modifier = Modifier.padding(vertical = 12.dp))
            TextField(
                modifier = Modifier.testTag("NameTextField"),
                value = state.name,
                onValueChange = { onEvent(LoginEvent.EditName(it)) },
                placeholder = { Text(text = stringResource(id = R.string.name_hint))}
            )
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                modifier = Modifier.testTag("SaveProfileButton"),
                onClick = { onEvent(LoginEvent.SaveProfile()) },
                enabled = state.isValidProfile
            ) {
                Text(text = stringResource(id = R.string.save_button_label))
            }

        }
    }
}

@Composable
fun ProviderSelection(
    state: LoginScreenState,
    onEvent: (LoginEvent) -> Unit
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("ProviderSelection"),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val loginErrorMessage = stringResource(id = R.string.could_not_sign_in_with_this_provider_error)
        GoogleSignIn(
            onIdTokenReceived = {onEvent(LoginEvent.GoogleLogin(it))},
            onFailure = {onEvent(LoginEvent.SetError(loginErrorMessage))}
        )
    }
}

@Composable
fun GoogleSignIn(
    onIdTokenReceived : (String) -> Unit,
    onFailure : () -> Unit
) {
    val context = LocalContext.current
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(BuildConfig.GOOGLE_SERVER_CLIENT_KEY)
        .build()
    val mGoogleSignInClient = com.google.android.gms.auth.api.signin.GoogleSignIn.getClient(context.findActivity(),gso)
    val intent = mGoogleSignInClient.signInIntent

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if(result.resultCode != RESULT_OK && result.resultCode != RESULT_CANCELED) {
            println("Auth LoginScreen: Google Sign In Failed! ResultCode: ${result.resultCode}")
            onFailure()
            return@rememberLauncherForActivityResult
        }
        val task = com.google.android.gms.auth.api.signin.GoogleSignIn.getSignedInAccountFromIntent(result.data)
        val resultAccount = task.getResult(ApiException::class.java)

        resultAccount?.idToken?.let { token ->
            println("Auth LoginScreen: google id token - $token")
            onIdTokenReceived(token)
        }
    }

    Button(onClick = { launcher.launch(intent) }) {
        Text(text = stringResource(id = R.string.sign_in_with_google_button_label))
    }
}