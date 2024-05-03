package com.example.ogueta_myweatherapp.view

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.FloatingActionButtonDefaults.elevation
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ogueta_myweatherapp.R
import com.example.ogueta_myweatherapp.models.UserViewModel
import com.example.ogueta_myweatherapp.ui.theme.FontTitle
import com.example.ogueta_myweatherapp.ui.theme.text

@Composable
fun Home(
    navController: NavController,
    userViewModel: UserViewModel,
) {

    LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
        item { HomeScreen(navController, userViewModel) }
    }

}

@Composable
fun HomeScreen(
    navController: NavController,
    userViewModel: UserViewModel,
) {

    var logged by remember { mutableStateOf(false) }
    if (userViewModel.auth.currentUser != null) {
        logged = true
    }

    var user by remember { mutableStateOf("") }

    if (logged) {
        userViewModel.getUser {
            user = it
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painterResource(id = R.drawable.homephoto),
            contentDescription = "Home Photo",
            modifier = Modifier.size(500.dp)
        )
        Text(
            text = if(logged) "Welcome $user!" else "Welcome User!",
            color = text,
            fontSize = 35.sp,
            fontFamily = FontTitle
        )

        if (!logged) {
            Column(modifier = Modifier.padding(15.dp)) {
                Button(onClick = {
                    navController.navigate("Login")
                },
                    modifier = Modifier.width(150.dp)
                    ) {
                    Text(text = "Log In", fontSize = 20.sp)
                }
                Button(onClick = {
                    navController.navigate("Register")
                }, Modifier.width(150.dp)) {
                    Text(text = "Register", fontSize = 20.sp)
                }
            }
        } else {
            val context = LocalContext.current
            Button(onClick = {
                userViewModel.auth.signOut()
                user = "User"
                logged = !logged
                Toast.makeText(
                    context,
                    "You have successfully logged out!",
                    Toast.LENGTH_SHORT
                ).show()
            }, modifier = Modifier.padding(15.dp).width(150.dp)) {
                Text(text = "Log Out", fontSize = 20.sp)
            }
        }
    }
}



