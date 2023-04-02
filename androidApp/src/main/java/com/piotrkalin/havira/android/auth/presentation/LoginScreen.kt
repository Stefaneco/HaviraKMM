package com.piotrkalin.havira.android.auth.presentation

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.piotrkalin.havira.android.BuildConfig
import com.piotrkalin.havira.android.core.presentation.LoadingScreen
import com.piotrkalin.havira.android.findActivity
import com.piotrkalin.havira.auth.presentation.LoginEvent
import com.piotrkalin.havira.auth.presentation.LoginScreenState
import com.piotrkalin.havira.auth.presentation.LoginStage.*
import com.piotrkalin.havira.core.domain.util.ImageFile
import kotlinx.coroutines.launch


@OptIn(ExperimentalCoilApi::class)
@Composable
fun LoginScreen(
    state: LoginScreenState,
    onEvent: (LoginEvent) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    state.error?.let {
        scope.launch {
            snackbarHostState.showSnackbar(it)
            onEvent(LoginEvent.OnErrorSeen)
        }
    }

    when(state.stage){
        WAITING_FOR_PROVIDER_SELECTION -> ProviderSelection(state = state, onEvent = onEvent)
        CONNECTING_TO_AZURE -> LoadingScreen()
        LOADING_PROFILE_INFO -> LoadingScreen()
        WAITING_FOR_PROFILE_CREATION -> LoginProfileCreation(state = state, onEvent = onEvent)
        CREATING_PROFILE -> LoadingScreen()
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

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val defaultPictureUri = Uri.parse("android.resource://com.piotrkalin.havira.android/drawable/default_picture")
        Spacer(modifier = Modifier.heightIn(100.dp).background(Color.Magenta))
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(state.image?.uri ?: defaultPictureUri)
                .transformations(CircleCropTransformation())
                .build(),
            contentDescription = "",
            modifier = Modifier
                .clip(CircleShape)
                .size(200.dp)
                .border(2.dp, Color.Gray, CircleShape)
                .clickable {
                    singlePhotoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
        )
        Spacer(modifier = Modifier.padding(vertical = 12.dp))
        TextField(
            value = state.name,
            onValueChange = { onEvent(LoginEvent.EditName(it)) },
            placeholder = { Text(text = "Name")}
        )
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { onEvent(LoginEvent.SaveProfile()) },
            enabled = state.isValidProfile
        ) {
            Text(text = "Save")
        }

    }
}

@Composable
fun ProviderSelection(
    state: LoginScreenState,
    onEvent: (LoginEvent) -> Unit
){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GoogleSignIn(
            onIdTokenReceived = {onEvent(LoginEvent.GoogleLogin(it))}
        )
    }
}

@Composable
fun GoogleSignIn(
    onIdTokenReceived : (String) -> Unit
) {
    val context = LocalContext.current
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(BuildConfig.GOOGLE_SERVER_CLIENT_KEY)
        .build()
    val mGoogleSignInClient = com.google.android.gms.auth.api.signin.GoogleSignIn.getClient(context.findActivity(),gso)
    val intent = mGoogleSignInClient.signInIntent

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        val task = com.google.android.gms.auth.api.signin.GoogleSignIn.getSignedInAccountFromIntent(it.data)
        val resultAccount = task.getResult(ApiException::class.java)
        resultAccount?.idToken?.let { token ->
            println("Auth LoginScreen: google id token - $token")
            onIdTokenReceived(token)
        }
    }

    Button(onClick = { launcher.launch(intent) }) {
        Text(text = "Sign In with Google")
    }
}