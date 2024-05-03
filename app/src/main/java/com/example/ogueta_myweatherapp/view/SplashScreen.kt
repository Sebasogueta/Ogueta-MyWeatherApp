package com.example.ogueta_myweatherapp.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.ogueta_myweatherapp.R
import com.example.ogueta_myweatherapp.ui.theme.background

@Composable
fun SplashScreen() {
    Surface(color = background) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painterResource(id = R.drawable.homephoto),
                contentDescription = "Home Photo",
                modifier = Modifier.size(350.dp)
            )
            CircularProgressIndicator(color = Color.Black, strokeWidth = 3.dp)
        }
    }
}
