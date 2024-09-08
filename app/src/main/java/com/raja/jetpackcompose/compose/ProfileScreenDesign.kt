package com.raja.jetpackcompose.compose

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.raja.jetpackcompose.R


@Composable
fun ProfileScreenDesign(isPreview : Boolean = false, navHostController: NavHostController) {
    var userName = ""
    var userEmailId = ""
    var userPhotoUrl = ""
    val context = LocalContext.current

    if(!isPreview){
        val firebaseAuth = remember { FirebaseAuth.getInstance() }
        val user by remember { mutableStateOf(firebaseAuth.currentUser) }
        userName = user?.displayName ?: ""
        userEmailId = user?.email ?: ""
        userPhotoUrl = (user?.photoUrl ?: "").toString()
    }

    var showLogOutDialog by remember {
        mutableStateOf(false)
    }

    var showExitDialog by remember { mutableStateOf(false) }

    BackHandler {
        showExitDialog = true


    }

    if(showExitDialog)
        ExitPopup()


    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.White), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = context.getString(R.string.welcome_splash), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.headlineMedium)

        Spacer(Modifier.height(8.dp))
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(userPhotoUrl)
                .crossfade(true)
                .placeholder(R.drawable.profile_pic)
                .build(),
            contentScale = ContentScale.Crop,
            contentDescription = null,
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .align(Alignment.CenterHorizontally)


        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(value = userName , onValueChange = {

        }, label = {
            Text(text = context.getString(R.string.name))
        }, keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number
        ),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
            , readOnly = true)

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(value = userEmailId , onValueChange = {
        }, label = {
            Text(text = context.getString(R.string.email_id))
        }, keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text
        ),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
            , readOnly = true
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
            showLogOutDialog = true
        }) {
            Text(text = context.getString(R.string.logout))
        }


        if(showLogOutDialog){
            Column(modifier = Modifier.padding(8.dp)) {
                AlertDialog(
                    onDismissRequest = {},
                    confirmButton = {
                        Text(text = context.getString(R.string.ok_button),fontSize = 16.sp,modifier = Modifier
                            .clickable {
                                showLogOutDialog = false

                                FirebaseAuth
                                    .getInstance()
                                    .signOut()
                                val googleSignInAccount =
                                    GoogleSignIn.getLastSignedInAccount(context)
                                if (googleSignInAccount != null) {
                                    // User is already signed in
                                    GoogleSignIn
                                        .getClient(context, GoogleSignInOptions.DEFAULT_SIGN_IN)
                                        .signOut()
                                }

                                navHostController.navigate("LoginScreen") {
                                    popUpTo(navHostController.graph.startDestinationId) {
                                        inclusive = true
                                    }
                                }


                            }
                            .padding(4.dp) )
                    },
                    dismissButton = {
                        Text(text = context.getString(R.string.cancel_button), fontSize = 16.sp, modifier = Modifier
                            .clickable {
                                showLogOutDialog = false
                            }
                            .padding(4.dp))
                    },
                    title = {
                        Text(text = context.getString(R.string.logout_app), fontSize = 18.sp)
                    },
                    properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true, usePlatformDefaultWidth = false),
                    modifier = Modifier.fillMaxWidth(0.9f)
                )
            }

        }
    }
}

@Composable
fun ExitPopup(){
    val context = LocalContext.current
    val activity = context as? Activity
    Column(modifier = Modifier.padding(8.dp)) {
        AlertDialog(
            onDismissRequest = {},
            confirmButton = {
                Text(text = context.getString(R.string.ok_button),fontSize = 16.sp,modifier = Modifier
                    .clickable {
                        activity?.finish()


                    }
                    .padding(4.dp) )
            },
            dismissButton = {
                Text(text = context.getString(R.string.cancel_button), fontSize = 16.sp, modifier = Modifier
                    .clickable {

                    }
                    .padding(4.dp))
            },
            title = {
                Text(text = context.getString(R.string.exit_app), fontSize = 18.sp)
            },
            properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true, usePlatformDefaultWidth = false),
            modifier = Modifier.fillMaxWidth(0.9f)
        )
    }


}

@Composable
@Preview
fun ProfileScreenDesignPreview(){
    ProfileScreenDesign(true, rememberNavController())
}