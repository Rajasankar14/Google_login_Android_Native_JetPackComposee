package com.raja.jetpackcompose

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.raja.jetpackcompose.navigation.NavigationScreens
import com.raja.jetpackcompose.ui.theme.GoogleLoginTheme
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GoogleLoginTheme {
                val navController = rememberNavController()

                Scaffold(modifier = Modifier.fillMaxSize(), content = {
                    NavigationScreens(navController)
                })



            }
        }
    }
}



@Composable
fun GoogleLoginScreen(navHostController: NavHostController, isPreview : Boolean = false) {
    val context = LocalContext.current

    var user by remember { mutableStateOf<FirebaseUser?>(null) }
    if(!isPreview)
        user  = Firebase.auth.currentUser

    val fireBaseLauncher = callGoogleAuthentication(

        onAuthComplete = { googleResult ->
            user = googleResult.user
            navHostController.navigate("ProfileScreen")

        }, onAuthError = {
            user = null
        })


    val activity = context as? Activity

    BackHandler {
        activity?.finish()
    }


    Column (modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
        Button(
            onClick = {
                val googleSignInOptions = GoogleSignInOptions
                    .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(context.getString(R.string.google_gmail_login))
                    .requestEmail()
                    .build()

                val googleSignInClient =
                    GoogleSignIn.getClient(context, googleSignInOptions)
                fireBaseLauncher.launch(googleSignInClient.signInIntent)
            }){
            Text(text = context.getString(R.string.login_with_google))

        }
    }

}


@Composable
fun callGoogleAuthentication(
    onAuthComplete :(AuthResult) -> Unit,
    onAuthError :(ApiException) -> Unit

) : ManagedActivityResultLauncher<Intent, ActivityResult> {
    val coroutineScope = rememberCoroutineScope()
    return rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result ->

        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)!!
            val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)
            coroutineScope.launch {
                val authResult = Firebase.auth.signInWithCredential(credential).await()
                onAuthComplete(authResult)
            }

        }catch (e : ApiException){
            onAuthError(e)
        }

    }
}




@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GoogleLoginTheme {
        GoogleLoginScreen(rememberNavController(), true)
    }
}