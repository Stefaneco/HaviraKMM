package com.piotrkalin.havira.android.auth.presentation

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.piotrkalin.havira.android.BuildConfig
import com.piotrkalin.havira.android.findActivity
import com.piotrkalin.havira.auth.presentation.LoginEvent
import com.piotrkalin.havira.auth.presentation.LoginScreenState

@Composable
fun LoginScreen(
    state: LoginScreenState,
    onEvent: (LoginEvent) -> Unit
) {
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