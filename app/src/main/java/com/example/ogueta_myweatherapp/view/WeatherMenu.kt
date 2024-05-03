package com.example.ogueta_myweatherapp.view

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.ogueta_myweatherapp.R
import com.example.ogueta_myweatherapp.models.UserViewModel
import com.example.ogueta_myweatherapp.models.Weather
import com.example.ogueta_myweatherapp.models.CityInfo
import com.example.ogueta_myweatherapp.models.CityViewModel
import com.example.ogueta_myweatherapp.models.WeatherViewModel
import com.example.ogueta_myweatherapp.ui.theme.FontText
import com.example.ogueta_myweatherapp.ui.theme.FontTitle
import com.example.ogueta_myweatherapp.ui.theme.background
import com.example.ogueta_myweatherapp.ui.theme.lightBlue2
import com.example.ogueta_myweatherapp.ui.theme.tertiary
import com.example.ogueta_myweatherapp.ui.theme.text
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt


val cityInfoList = mutableStateListOf<CityInfo>()


@Composable
fun WeatherMenu(
    navController: NavHostController,
    userViewModel: UserViewModel,
    cityViewModel: CityViewModel,
) {
    val weatherViewModel: WeatherViewModel = viewModel()
    var cityInput by remember { mutableStateOf("") }
    cityInput = cityViewModel.getCity()
    cityViewModel.setCity("")
    Column(modifier = Modifier.fillMaxSize()) {

        LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
            item {
                Text(
                    text = "Search now!",
                    fontSize = 30.sp,
                    modifier = Modifier.padding(16.dp),
                    color = text,
                    fontFamily = FontTitle
                )
            }
            item { WeatherInfo(userViewModel, cityInput, weatherViewModel) }
        }

    }

}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationGraphicsApi::class)
@SuppressLint("SuspiciousIndentation", "UnrememberedMutableState")
@Composable
fun WeatherInfo(
    userViewModel: UserViewModel,
    cityParameter: String,
    weatherViewModel: WeatherViewModel
) {

    var logged by remember { mutableStateOf(false) }
    if (userViewModel.auth.currentUser != null) {
        logged = true
    }

    var cityInput by remember { mutableStateOf(cityParameter) }

    var location by remember { mutableStateOf("") }
    var search by remember { mutableStateOf(false) }
    var firstTime by remember { mutableStateOf(true) }

    val systems = listOf("metric", "imperial")

    var selectedSystem by rememberSaveable { mutableStateOf(systems[0]) }

    var loading by remember { mutableFloatStateOf(1f) } //alpha to 0 when loading

    if (cityParameter.isNotEmpty() && firstTime) {
        location = cityParameter
        search = !search
        firstTime = false
    }

    Column {
        Row {
            OutlinedTextField(
                value = cityInput,
                onValueChange = { cityInput = it },
                label = {
                    Text("Introduce the location: ")
                },
                modifier = Modifier
                    .weight(4f),
                singleLine = true,
            )
            var atEnd by remember { mutableStateOf(false) }
            IconButton(
                onClick = {
                    location = cityInput
                    search = !search
                    firstTime = false
                    atEnd = !atEnd
                }, modifier = Modifier
                    .weight(1f)
                    .padding(top = 10.dp)
            ) {
                val image = AnimatedImageVector.animatedVectorResource(R.drawable.search_rotation)
                Image(painter = rememberAnimatedVectorPainter(image,atEnd),
                    contentDescription = "Rotation search icon",
                    contentScale = ContentScale.Crop
                )
//                Icon(
//                    imageVector = Icons.Default.Search,
//                    contentDescription = "Search",
//                    modifier = Modifier.size(70.dp)
//                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text("Measurement system: ", fontSize = 20.sp, color = text, fontFamily = FontText, modifier = Modifier.padding(10.dp))
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
                selected = selectedSystem == systems[0],
                onClick = { selectedSystem = systems[0] },
            )
            Text(
                systems[0].capitalize(),
                fontSize = 20.sp,
                modifier = Modifier.weight(1f),
                color = text
            )
            Spacer(modifier = Modifier.weight(0.5f))
            RadioButton(
                selected = selectedSystem == systems[1],
                onClick = { selectedSystem = systems[1] },
            )
            Text(
                systems[1].capitalize(),
                fontSize = 20.sp,
                modifier = Modifier.weight(1f),
                color = text
            )
        }


    }

    Column(modifier = Modifier.alpha(loading)) {


        //Search of the city
        val context = LocalContext.current

        val url =
            "http://api.openweathermap.org/geo/1.0/direct?q=$location&limit=1&appid=1230d9bf303f1a4db29448e97db5566e"
        if (location.isNotEmpty()) {
            LaunchedEffect(search, firstTime) {
                searchCity(
                    url,
                    context
                ) //borra cityInfoList y le agrega un elemento (si no da error)
            }

            if (cityInfoList.isNotEmpty()) {

                val cityInfo = cityInfoList[0]


                val url2 =
                    "https://api.tomorrow.io/v4/timelines?location=${cityInfo.coordinates}&fields=temperature,temperatureApparent,humidity,windSpeed,windDirection,pressureSurfaceLevel,precipitationIntensity,weatherCode,temperatureMin,temperatureMax,precipitationProbability&timesteps=1d&units=$selectedSystem&apikey=Dbn0oZpk1tUsdvbiQqoKfDD8Hw3dHYDp"
                LaunchedEffect(cityInfo.city, selectedSystem) {
                        loading = 0f
                        listWeather(
                            "${cityInfo.city},${cityInfo.country}",
                            url2,
                            context,
                            weatherViewModel
                        )
                        loading = 1f
                }


                if (weatherViewModel.getWeatherList().isNotEmpty()) {

                    Column {
                        Text(
                            text = "${cityInfo.city}, ${countryCodes[cityInfo.country]}",
                            fontSize = 30.sp,
                            color = text
                        )

                        var index = (weatherViewModel.getDay() - weatherViewModel.getFirstDay())

                        //detailed weather

                        ItemDetailedWeather(
                            weather = weatherViewModel.getWeatherList()[index],
                            selectedSystem
                        )


                        LazyRow {
                            //weather 6 days

                            items(weatherViewModel.getWeatherList()) { forecastWeather ->
                                ItemForecastWeather(
                                    weather = forecastWeather,
                                    selectedSystem,
                                    weatherViewModel
                                )

                            }
                        }
                        if (logged) {
                            var favoriteCityList = remember { mutableStateListOf<String>() }

                            userViewModel.getFavoriteCities { cityList ->
                                favoriteCityList.addAll(cityList)
                            }

                            var favorite =
                                "${cityInfo.city}, ${countryCodes[cityInfo.country]}" in favoriteCityList //boolean
                            var heartIcon =
                                if (favorite) R.drawable.filled_heart else R.drawable.outlined_heart

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(onClick = {
                                    favorite = !favorite
                                    if (!favorite) userViewModel.removeFavorite(
                                        favoriteCityList,
                                        "${cityInfo.city}, ${countryCodes[cityInfo.country]}"
                                    ) { newList ->
                                        favoriteCityList.clear()
                                        favoriteCityList.addAll(newList)
                                    } else userViewModel.addFavorite(
                                        favoriteCityList,
                                        "${cityInfo.city}, ${countryCodes[cityInfo.country]}"
                                    ) { newList ->
                                        favoriteCityList.clear()
                                        favoriteCityList.addAll(newList)
                                    }
                                }) {
                                    Icon(
                                        painter = painterResource(id = heartIcon),
                                        contentDescription = "Heart",
                                        tint = Color.Red, modifier = Modifier.size(75.dp)
                                    )
                                }
                                Text(
                                    text = if (favorite) "Remove from favorites" else "Add to favorites",
                                    color = text
                                )

                            }
                        }


                    }
                }
            }

        } else {
            var errorMessage by remember { mutableStateOf("") }
            errorMessage = if (firstTime) {
                ""
            } else {
                "Location not found, try again"
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(text = errorMessage, fontSize = 20.sp, color = text)
            }

        }
    }
}


@Composable
fun ItemDetailedWeather(weather: Weather, measurementSystem: String) {

    val temperatureUnit = if (measurementSystem == "metric") "ºC" else "ºF"
    val speedUnit = if (measurementSystem == "metric") "km/h" else "m/h"
    val pressureUnit = if (measurementSystem == "metric") "hPa" else "inHg"
    val precipitationIntensityUnit =
        if (measurementSystem == "metric") "mm/h" else "in/h"
    val windDirectionText = getWindDirection(weather.windDirection)

    Box(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
            .background(tertiary)
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painterResource(id = getWeatherCodeImg(weather.weatherCode)),
                    contentDescription = "weather state",
                    Modifier
                        .size(75.dp)
                        .weight(1f)
                )
                Text(
                    text = "${weather.temperature}$temperatureUnit",
                    fontSize = 40.sp,
                    modifier = Modifier.weight(1f),
                    color = background
                )
                Column(modifier = Modifier.weight(1.5f)) {
                    Text(
                        text = getDayOfTheWeek(weather.date),
                        textAlign = TextAlign.Left,
                        color = background
                    )
                    Text(
                        text = getWeatherCodeText(weather.weatherCode), color = background
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            // Debajo: información adicional del clima
            Text(
                text = "Temperature Apparent: ${weather.temperatureApparent}$temperatureUnit",
                color = background
            )
            Text(
                text = "Wind ${windDirectionText}, ${weather.windSpeed} $speedUnit",
                color = background
            )
            Text(text = "Humidity: ${weather.humidity}%", color = background)
            Text(
                text = "Precipitation Intensity: ${weather.precipitationIntensity} $precipitationIntensityUnit",
                color = background
            )
            Text(
                text = "Pressure Surface Level: ${weather.pressureSurfaceLevel} $pressureUnit",
                color = background
            )
        }
    }
}


@Composable
fun ItemForecastWeather(
    weather: Weather,
    measurementSystem: String,
    weatherViewModel: WeatherViewModel
) {

    val temperatureUnit = if (measurementSystem == "metric") "ºC" else "ºF"


    val day = weather.date.substring(8, 10).toInt()

    Box(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
            .background(tertiary)
            .clickable(onClick = { weatherViewModel.setDay(day) }),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(5.dp)
        ) {

            Text(text = getDayOfTheWeek(weather.date), color = background)
            Image(
                painterResource(id = getWeatherCodeImg(weather.weatherCode)),
                contentDescription = "weather state",
                Modifier.size(100.dp)
            )
            Text(
                text = "Max: ${weather.temperatureMax}$temperatureUnit, Min: ${weather.temperatureMin}$temperatureUnit",
                color = background
            )
            Text(
                text = "Probability of Precipitation: ${weather.precipitationProbability}%",
                color = background
            )
            Text(text = getWeatherCodeText(weather.weatherCode), color = background)
        }

    }
}

fun searchCity(url: String, context: Context) {

    cityInfoList.clear()
    val requestQueue = Volley.newRequestQueue(context)
    val jsonArrayRequest = JsonArrayRequest(
        Request.Method.GET,
        url,
        null,
        { response ->
            val firstObject = response?.optJSONObject(0)

            val cityName = firstObject?.optString("name")
            val coordinates = "${firstObject?.optString("lat")},${firstObject?.getString("lon")}"
            val countryCode = firstObject?.optString("country")
            if (cityName != null && coordinates != null && countryCode != null) {
                cityInfoList.add(CityInfo(cityName, coordinates, countryCode))
            }
        },
        { error ->

        }
    )
    requestQueue.add(jsonArrayRequest)
}

fun listWeather(city: String, url: String, context: Context, weatherViewModel: WeatherViewModel) {

    val weatherList = mutableStateListOf<Weather>()

    val requestQueue = Volley.newRequestQueue(context)
    val jsonObjectRequest = JsonObjectRequest(
        Request.Method.GET,
        url,
        null,
        { response ->
            val dataObject = response.optJSONObject("data")
            val timelinesArray = dataObject?.optJSONArray("timelines")

            timelinesArray?.let {
                for (i in 0 until it.length()) {
                    val timelineObject = it.optJSONObject(i)
                    val intervalsArray = timelineObject?.optJSONArray("intervals")

                    intervalsArray?.let { intervals ->
                        for (j in 0 until intervals.length()) {
                            val dayObject = intervals.optJSONObject(j)
                            var date = dayObject.optString("startTime")
                            val valuesObject = dayObject.optJSONObject("values")

                            valuesObject?.let { values ->
                                val humidity = values.optDouble("humidity").roundToInt()
                                val precipitationIntensity =
                                    values.optDouble("precipitationIntensity").roundToInt()
                                val precipitationProbability =
                                    values.optInt("precipitationProbability")
                                val pressureSurfaceLevel =
                                    values.optDouble("pressureSurfaceLevel").roundToInt()
                                val temperature = values.optDouble("temperature").roundToInt()
                                val temperatureApparent =
                                    values.optDouble("temperatureApparent").roundToInt()
                                val temperatureMax = values.optDouble("temperatureMax").roundToInt()
                                val temperatureMin = values.optDouble("temperatureMin").roundToInt()
                                val weatherCode = values.optInt("weatherCode")
                                val windDirection = values.optDouble("windDirection")
                                val windSpeed = values.optDouble("windSpeed").roundToInt()

                                date = date.substring(0, 10)

                                val weather = Weather(
                                    date,
                                    city,
                                    humidity,
                                    precipitationIntensity,
                                    precipitationProbability,
                                    pressureSurfaceLevel,
                                    temperature,
                                    temperatureApparent,
                                    temperatureMax,
                                    temperatureMin,
                                    weatherCode,
                                    windDirection,
                                    windSpeed
                                )
                                weatherList.add(weather)
                            }
                        }
                    }
                }

                weatherViewModel.setFirstDay(
                    weatherList[0].date.substring(
                        8,
                        10
                    ).toInt()
                )
                weatherViewModel.setDay(
                    weatherList[0].date.substring(8, 10).toInt()
                )
                weatherViewModel.setWeatherList(weatherList)
            }
        },
        { error ->
        }
    )
    requestQueue.add(jsonObjectRequest)
}

fun getDayOfTheWeek(date: String): String {
    val day = date.substring(8, 10)
    val localDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE)
    val dayOfWeek = localDate.dayOfWeek
    return "${dayOfWeek.toString().capitalize()} $day"
}


fun getWindDirection(windDirection: Double): String {
    val direction = when {
        (windDirection in 0.0..22.5) || (windDirection < 337.5 && windDirection <= 360.0) -> "N"
        windDirection > 22.5 && windDirection <= 67.5 -> "NE"
        windDirection > 67.5 && windDirection <= 112.5 -> "E"
        windDirection > 112.5 && windDirection <= 157.5 -> "SE"
        windDirection > 157.5 && windDirection <= 202.5 -> "S"
        windDirection > 202.5 && windDirection <= 247.5 -> "SW"
        windDirection > 247.5 && windDirection <= 292.5 -> "W"
        windDirection > 292.5 && windDirection >= 337.5 -> "NW"
        else -> "Error"
    }
    return direction
}

fun getWeatherCodeText(weatherCode: Int): String {
    val weatherText = when (weatherCode) {
        0 -> "Unknown"
        1000 -> "Clear, Sunny"
        1100 -> "Mostly Clear"
        1101 -> "Partly Cloudy"
        1102 -> "Mostly Cloudy"
        1001 -> "Cloudy"
        2000 -> "Fog"
        2100 -> "Light Fog"
        4000 -> "Drizzle"
        4001 -> "Rain"
        4200 -> "Light Rain"
        4201 -> "Heavy Rain"
        5000 -> "Snow"
        5001 -> "Flurries"
        5100 -> "Light Snow"
        5101 -> "Heavy Snow"
        6000 -> "Freezing Drizzle"
        6001 -> "Freezing Rain"
        6200 -> "Light Freezing Rain" // Drizzle
        6201 -> "Heavy Freezing Rain"
        7000 -> "Ice Pellets"
        7101 -> "Heavy Ice Pellets"
        7102 -> "Light Ice Pellets"
        8000 -> "Thunderstorm"
        else -> "Invalid Weather Code"
    }

    return weatherText
}

fun getWeatherCodeImg(weatherCode: Int): Int {
    val weatherImg = when (weatherCode) {
        0 -> R.drawable.notavailable //unknown
        1000 -> R.drawable.clear
        1100 -> R.drawable.mostlyclear
        1101 -> R.drawable.partlycloudy
        1102 -> R.drawable.mostlycloudy
        1001 -> R.drawable.cloudy
        2000 -> R.drawable.fog
        2100 -> R.drawable.lightfog
        4000 -> R.drawable.drizzle
        4001 -> R.drawable.rain
        4200 -> R.drawable.lightrain
        4201 -> R.drawable.heavyrain
        5000 -> R.drawable.snow
        5001 -> R.drawable.flurries
        5100 -> R.drawable.lightsnow
        5101 -> R.drawable.heavysnow
        6000 -> R.drawable.freezingdrizzle
        6001 -> R.drawable.freezingrain
        6200 -> R.drawable.lightfreezingdrizzle
        6201 -> R.drawable.heavyfreezingrain
        7000 -> R.drawable.icepellets
        7101 -> R.drawable.heavyicepellets
        7102 -> R.drawable.lighticepellets
        8000 -> R.drawable.thunderstorm
        else -> R.drawable.notavailable //error
    }

    return weatherImg
}


val countryCodes = mapOf(
    "AF" to "Afghanistan",
    "AX" to "Åland Islands",
    "AL" to "Albania",
    "DZ" to "Algeria",
    "AS" to "American Samoa",
    "AD" to "Andorra",
    "AO" to "Angola",
    "AI" to "Anguilla",
    "AQ" to "Antarctica",
    "AG" to "Antigua and Barbuda",
    "AR" to "Argentina",
    "AM" to "Armenia",
    "AW" to "Aruba",
    "AU" to "Australia",
    "AT" to "Austria",
    "AZ" to "Azerbaijan",
    "BH" to "Bahrain",
    "BS" to "Bahamas",
    "BD" to "Bangladesh",
    "BB" to "Barbados",
    "BY" to "Belarus",
    "BE" to "Belgium",
    "BZ" to "Belize",
    "BJ" to "Benin",
    "BM" to "Bermuda",
    "BT" to "Bhutan",
    "BO" to "Bolivia",
    "BQ" to "Bonaire, Sint Eustatius and Saba",
    "BA" to "Bosnia and Herzegovina",
    "BW" to "Botswana",
    "BV" to "Bouvet Island",
    "BR" to "Brazil",
    "IO" to "British Indian Ocean Territory",
    "BN" to "Brunei Darussalam",
    "BG" to "Bulgaria",
    "BF" to "Burkina Faso",
    "BI" to "Burundi",
    "KH" to "Cambodia",
    "CM" to "Cameroon",
    "CA" to "Canada",
    "CV" to "Cape Verde",
    "KY" to "Cayman Islands",
    "CF" to "Central African Republic",
    "TD" to "Chad",
    "CL" to "Chile",
    "CN" to "China",
    "CX" to "Christmas Island",
    "CC" to "Cocos (Keeling) Islands",
    "CO" to "Colombia",
    "KM" to "Comoros",
    "CG" to "Congo",
    "CD" to "Democratic Republic of the Congo",
    "CK" to "Cook Islands",
    "CR" to "Costa Rica",
    "CI" to "Côte d'Ivoire",
    "HR" to "Croatia",
    "CU" to "Cuba",
    "CW" to "Curaçao",
    "CY" to "Cyprus",
    "CZ" to "Czech Republic",
    "DK" to "Denmark",
    "DJ" to "Djibouti",
    "DM" to "Dominica",
    "DO" to "Dominican Republic",
    "EC" to "Ecuador",
    "EG" to "Egypt",
    "SV" to "El Salvador",
    "GQ" to "Equatorial Guinea",
    "ER" to "Eritrea",
    "EE" to "Estonia",
    "ET" to "Ethiopia",
    "FK" to "Malvinas Islands",
    "FO" to "Faroe Islands",
    "FJ" to "Fiji",
    "FI" to "Finland",
    "FR" to "France",
    "GF" to "French Guiana",
    "PF" to "French Polynesia",
    "TF" to "French Southern Territories",
    "GA" to "Gabon",
    "GM" to "Gambia",
    "GE" to "Georgia",
    "DE" to "Germany",
    "GH" to "Ghana",
    "GI" to "Gibraltar",
    "GR" to "Greece",
    "GL" to "Greenland",
    "GD" to "Greenland",
    "GP" to "Guadeloupe",
    "GU" to "Guam",
    "GT" to "Guatemala",
    "GG" to "Guernsey",
    "GN" to "Guinea",
    "GW" to "Guinea-Bissau",
    "GY" to "Guyana",
    "HT" to "Haiti",
    "HM" to "Heard Island and McDonald Islands",
    "VA" to "Holy See (Vatican City State)",
    "HN" to "Honduras",
    "HK" to "Hong Kong",
    "HU" to "Hungary",
    "IS" to "Iceland",
    "IN" to "India",
    "ID" to "Indonesia",
    "IR" to "Iran",
    "IQ" to "Iraq",
    "IE" to "Ireland",
    "IM" to "Isle of Man",
    "IL" to "Israel",
    "IT" to "Italy",
    "JM" to "Jamaica",
    "JP" to "Japan",
    "JE" to "Jersey",
    "JO" to "Jordan",
    "KZ" to "Kazakhstan",
    "KE" to "Kenya",
    "KI" to "Kiribati",
    "KP" to "Democratic People's Republic of Korea",
    "KR" to "Republic of Korea",
    "KW" to "Kuwait",
    "KG" to "Kyrgyzstan",
    "LA" to "Lao People's Democratic Republic",
    "LV" to "Latvia",
    "LB" to "Lebanon",
    "LS" to "Lesotho",
    "LR" to "Liberia",
    "LY" to "Libya",
    "LI" to "Liechtenstein",
    "LT" to "Lithuania",
    "LU" to "Luxembourg",
    "MO" to "Macao",
    "MK" to "Macedonia",
    "MG" to "Madagascar",
    "MW" to "Malawi",
    "MY" to "Malaysia",
    "MV" to "Maldives",
    "ML" to "Mali",
    "MT" to "Malta",
    "MH" to "Marshall Islands",
    "MQ" to "Martinique",
    "MR" to "Mauritania",
    "MU" to "Mauritius",
    "YT" to "Mayotte",
    "MX" to "Mexico",
    "FM" to "Federated States of Micronesia",
    "MD" to "Republic of Moldova",
    "MC" to "Monaco",
    "MN" to "Mongolia",
    "ME" to "Montenegro",
    "MS" to "Montserrat",
    "MA" to "Morocco",
    "MZ" to "Mozambique",
    "MM" to "Myanmar",
    "NA" to "Namibia",
    "NR" to "Nauru",
    "NP" to "Nepal",
    "NL" to "Netherlands",
    "NC" to "New Caledonia",
    "NZ" to "New Zealand",
    "NI" to "Nicaragua",
    "NE" to "Niger",
    "NG" to "Nigeria",
    "NU" to "Niue",
    "NF" to "Norfolk Island",
    "MP" to "Northern Mariana Islands",
    "NO" to "Norway",
    "OM" to "Oman",
    "PK" to "Pakistan",
    "PW" to "Palau",
    "PS" to "Palestine",
    "PA" to "Panama",
    "PG" to "Papua New Guinea",
    "PY" to "Paraguay",
    "PE" to "Peru",
    "PH" to "Philippines",
    "PN" to "Pitcairn",
    "PL" to "Poland",
    "PT" to "Portugal",
    "PR" to "Puerto Rico",
    "QA" to "Qatar",
    "RE" to "Réunion",
    "RO" to "Romania",
    "RU" to "Russian Federation",
    "RW" to "Rwanda",
    "BL" to "Saint Barthélemy",
    "SH" to "Saint Helena, Ascension and Tristan da Cunha",
    "KN" to "Saint Kitts and Nevis",
    "LC" to "Saint Lucia",
    "MF" to "Saint Martin (French part)",
    "PM" to "Saint Pierre and Miquelon",
    "VC" to "Saint Vincent and the Grenadines",
    "WS" to "Samoa",
    "SM" to "San Marino",
    "ST" to "Sao Tome and Principe",
    "SA" to "Saudi Arabia",
    "SN" to "Senegal",
    "RS" to "Serbia",
    "SC" to "Seychelles",
    "SL" to "Sierra Leone",
    "SG" to "Singapore",
    "SX" to "Sint Maarten (Dutch part)",
    "SK" to "Slovakia",
    "SI" to "Slovenia",
    "SB" to "Solomon Islands",
    "SO" to "Somalia",
    "ZA" to "South Africa",
    "GS" to "South Georgia and the South Sandwich Islands",
    "SS" to "South Sudan",
    "ES" to "Spain",
    "LK" to "Sri Lanka",
    "SD" to "Sudan",
    "SR" to "Suriname",
    "SJ" to "Svalbard and Jan Mayen",
    "SZ" to "Swaziland",
    "SE" to "Sweden",
    "CH" to "Switzerland",
    "SY" to "Syrian Arab Republic",
    "TW" to "Taiwan",
    "TJ" to "Tajikistan",
    "TZ" to "United Republic of Tanzania",
    "TH" to "Thailand",
    "TL" to "Timor-Leste",
    "TG" to "Togo",
    "TK" to "Tokelau",
    "TO" to "Tonga",
    "TT" to "Trinidad and Tobago",
    "TN" to "Tunisia",
    "TR" to "Turkey",
    "TM" to "Turkmenistan",
    "TC" to "Turks and Caicos Islands",
    "TV" to "Tuvalu",
    "UG" to "Uganda",
    "UA" to "Ukraine",
    "AE" to "United Arab Emirates",
    "GB" to "United Kingdom",
    "US" to "United States",
    "UM" to "United States Minor Outlying Islands",
    "UY" to "Uruguay",
    "UZ" to "Uzbekistan",
    "VU" to "Vanuatu",
    "VE" to "Bolivarian Republic of Venezuela",
    "VN" to "Viet Nam",
    "VG" to "Virgin Islands, British",
    "VI" to "Virgin Islands, U.S.",
    "WF" to "Wallis and Futuna",
    "EH" to "Western Sahara",
    "YE" to "Yemen",
    "ZM" to "Zambia",
    "ZW" to "Zimbabwe"
)