package com.example.ogueta_myweatherapp.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ogueta_myweatherapp.models.CityViewModel
import com.example.ogueta_myweatherapp.models.UserViewModel
import com.example.ogueta_myweatherapp.ui.theme.FontText
import com.example.ogueta_myweatherapp.ui.theme.FontTitle
import com.example.ogueta_myweatherapp.ui.theme.background
import com.example.ogueta_myweatherapp.ui.theme.primary
import com.example.ogueta_myweatherapp.ui.theme.secondary
import com.example.ogueta_myweatherapp.ui.theme.tertiary
import com.example.ogueta_myweatherapp.ui.theme.text


@Composable
fun Favorites(
    navController: NavHostController,
    userViewModel: UserViewModel,
    cityViewModel: CityViewModel,
) {

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {

        var logged by remember { mutableStateOf(false) }
        if (userViewModel.auth.currentUser != null) {
            logged = true
        }

        Text(text = "Your favorite places!", fontSize = 30.sp, modifier = Modifier.padding(16.dp), color = text, fontFamily = FontTitle)

        if (logged) {


                var favoriteCities = remember { mutableStateListOf<String>() }

                userViewModel.getFavoriteCities { list ->
                    favoriteCities.clear()
                    for(city in list){
                        favoriteCities.add(city)
                    }
                }


            if (favoriteCities.isNotEmpty()) {

                LazyColumn {

                    items(favoriteCities) { favoriteCity ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(3.dp, primary)
                                .background(tertiary)
                        ) {
                            Row(modifier = Modifier.clickable(onClick = {
                                cityViewModel.setCity(favoriteCity)
                                navController.navigate("WeatherMenu")
                            })) {
                                Text(
                                    text = favoriteCity, modifier = Modifier
                                        .weight(4f)
                                        .padding(top = 12.dp, start = 16.dp),
                                    color = background,
                                    fontSize = 19.sp
                                )
                                IconButton(
                                    onClick = {
                                        userViewModel.removeFavorite(
                                            favoriteCities,
                                            favoriteCity
                                        ) { cityList ->
                                            favoriteCities.clear()
                                            favoriteCities.addAll(cityList)

                                        }
                                    },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Favorite,
                                        contentDescription = "",
                                        tint = Color.Red,
                                        modifier = Modifier.size(40.dp)
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "You have no favorite cities.", color = text, fontFamily = FontText, fontSize = 20.sp, textAlign = TextAlign.Center)
                }
            }

        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "You have to be logged in to use this feature.", color = text, fontFamily = FontText, fontSize = 20.sp, textAlign = TextAlign.Center)
            }
        }
    }
}





