package com.raja.jetpackcompose.compose

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.raja.jetpackcompose.R
import kotlinx.coroutines.delay

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SplashScreenDesign(navHostController : NavHostController,isNotPreview: Boolean = true) {
    val context = LocalContext.current
    Scaffold (content = {
        if(isNotPreview){
            val fireBaseAuth = remember {
                FirebaseAuth.getInstance()
            }
            val fireBaseUser  by remember {
                mutableStateOf(fireBaseAuth.currentUser)
            }

            LaunchedEffect(key1 = fireBaseUser) {
                delay(4000)
                navHostController.let {
                    if (it.graph.startDestinationRoute != null) { // Ensure the graph is set
                        if (fireBaseUser != null) {
                            it.navigate("ProfileScreen") {
                                popUpTo("SplashScreen") { inclusive = true }
                            }
                        } else {
                            it.navigate("LoginScreen") {
                                popUpTo("SplashScreen") { inclusive = true }
                            }
                        }
                    }
                }
            }

        }
        Column(modifier = Modifier
            .fillMaxSize()
            .background(Color.White), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.google),
                alignment = Alignment.Center,
                contentDescription = "",
                modifier = Modifier
                    .wrapContentWidth()
                    .size(50.dp)
                    .align(Alignment.CenterHorizontally))

            Spacer(modifier = Modifier.height(10.dp))
            Text(text = context.getString(R.string.google_signin), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.headlineMedium)

        }
    },
        bottomBar = {
            Column(modifier = Modifier.navigationBarsPadding().padding(8.dp)) {
                Text(text = context.getString(R.string.developer_txt), color = Color.Black, fontWeight = FontWeight.Light, style = MaterialTheme.typography.bodySmall, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
            }

        })

}

@Composable
@Preview
fun SplashScreenPreview(){
    SplashScreenDesign(rememberNavController(),false)
}