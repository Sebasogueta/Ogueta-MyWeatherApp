package com.example.ogueta_myweatherapp.models

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ogueta_myweatherapp.R
import com.example.ogueta_myweatherapp.ui.theme.OguetaMyWeatherAppTheme
import com.example.ogueta_myweatherapp.ui.theme.bottombar
import com.example.ogueta_myweatherapp.view.Favorites
import com.example.ogueta_myweatherapp.view.Home
import com.example.ogueta_myweatherapp.view.Login
import com.example.ogueta_myweatherapp.view.Register
import com.example.ogueta_myweatherapp.view.SplashScreen
import com.example.ogueta_myweatherapp.view.WeatherMenu
import kotlinx.coroutines.delay


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val userViewModel: UserViewModel = viewModel()
            val cityViewModel: CityViewModel = viewModel()


            var isLoading by remember { mutableStateOf(true) }

            LaunchedEffect(Unit) {
                delay(1000)
                isLoading = false
            }
            OguetaMyWeatherAppTheme {

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = { MyBottomNavigation(navController) }
                )
                {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                top = 16.dp,
                                start = 16.dp,
                                end = 16.dp,
                                bottom = it.calculateBottomPadding()
                            )
                    ) {
                        // Usar AnimatedVisibility para mostrar el SplashScreen solo mientras isLoading es verdadero
                        AnimatedVisibility(visible = isLoading) {
                            SplashScreen()
                        }

                        // Una vez que isLoading sea falso, mostrar el contenido principal
                        AnimatedVisibility(visible = !isLoading) {


                            NavHost(
                                navController = navController, startDestination = "Home"
                            ) {

                                composable("Favorites",
                                    enterTransition = {
                                        slideIntoContainer(
                                            towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                                            animationSpec = tween(700)
                                        )
                                    },
                                    exitTransition = {
                                        slideOutOfContainer(
                                            towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                                            animationSpec = tween(700)
                                        )
                                    },
                                    popEnterTransition = {
                                        slideIntoContainer(
                                            towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                                            animationSpec = tween(700)
                                        )
                                    },
                                    popExitTransition = {
                                        slideOutOfContainer(
                                            towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                                            animationSpec = tween(700)
                                        )
                                    }) {
                                    Favorites(
                                        navController = navController,
                                        userViewModel,
                                        cityViewModel
                                    )
                                }

                                composable("Home") {
                                    Home(
                                        navController = navController,
                                        userViewModel
                                    )
                                }

                                composable("Login",
                                    enterTransition = {
                                        slideIntoContainer(
                                            towards = AnimatedContentTransitionScope.SlideDirection.Companion.Up,
                                            animationSpec = tween(700)
                                        )
                                    },
                                    exitTransition = {
                                        slideOutOfContainer(
                                            towards = AnimatedContentTransitionScope.SlideDirection.Companion.Down,
                                            animationSpec = tween(700)
                                        )
                                    },
                                    popEnterTransition = {
                                        slideIntoContainer(
                                            towards = AnimatedContentTransitionScope.SlideDirection.Companion.Up,
                                            animationSpec = tween(700)
                                        )
                                    },
                                    popExitTransition = {
                                        slideOutOfContainer(
                                            towards = AnimatedContentTransitionScope.SlideDirection.Companion.Down,
                                            animationSpec = tween(700)
                                        )
                                    }) { Login(navController = navController, userViewModel) }
                                composable("Register",
                                    enterTransition = {
                                        slideIntoContainer(
                                            towards = AnimatedContentTransitionScope.SlideDirection.Companion.Up,
                                            animationSpec = tween(700)
                                        )
                                    },
                                    exitTransition = {
                                        slideOutOfContainer(
                                            towards = AnimatedContentTransitionScope.SlideDirection.Companion.Down,
                                            animationSpec = tween(700)
                                        )
                                    },
                                    popEnterTransition = {
                                        slideIntoContainer(
                                            towards = AnimatedContentTransitionScope.SlideDirection.Companion.Up,
                                            animationSpec = tween(700)
                                        )
                                    },
                                    popExitTransition = {
                                        slideOutOfContainer(
                                            towards = AnimatedContentTransitionScope.SlideDirection.Companion.Down,
                                            animationSpec = tween(700)
                                        )
                                    }) { Register(navController = navController, userViewModel) }
                                composable("WeatherMenu",
                                    enterTransition = {
                                        slideIntoContainer(
                                            towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                                            animationSpec = tween(700)
                                        )
                                    },
                                    exitTransition = {
                                        slideOutOfContainer(
                                            towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                                            animationSpec = tween(700)
                                        )
                                    },
                                    popEnterTransition = {
                                        slideIntoContainer(
                                            towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                                            animationSpec = tween(700)
                                        )
                                    },
                                    popExitTransition = {
                                        slideOutOfContainer(
                                            towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                                            animationSpec = tween(700)
                                        )
                                    }) {
                                    WeatherMenu(
                                        navController = navController,
                                        userViewModel,
                                        cityViewModel
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}



@Composable
fun MyBottomNavigation(navController: NavHostController) {

    var selectedItem by remember { mutableStateOf("Home") }

    BottomNavigation(backgroundColor = bottombar, contentColor = Color.White) {
        BottomNavigationItem(
            selected = selectedItem == "Favorites",
            onClick = {
                    selectedItem = "Favorites"
                    navController.navigate("Favorites")
                      },

            icon = {

                Icon(
                    painter = painterResource(id = if (selectedItem == "Favorites") {
                        R.drawable.filled_heart
                    } else {
                        R.drawable.outlined_heart
                    }),
                    contentDescription = "Favorites",
                     tint = Color.White
                )
            },
            label = { Text("Favorites",  color = Color.White) })

        BottomNavigationItem(
            selected = selectedItem == "Home",
            onClick = {
                selectedItem = "Home"
                navController.navigate("Home") },
            icon = {
                Icon(
                    painter = painterResource(id = if (selectedItem == "Home") {
                        R.drawable.filled_home
                    } else {
                        R.drawable.outlined_home
                    }),
                    contentDescription = "Home",
                    tint= Color.White
                )
            },
            label = { Text("Home", color = Color.White) })

        BottomNavigationItem(
            selected = selectedItem == "WeatherMenu",
            onClick = {
                selectedItem = "WeatherMenu"
                navController.navigate("WeatherMenu") },
            icon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.White
                )
            },
            label = { Text("Search", color = Color.White) })
    }
}