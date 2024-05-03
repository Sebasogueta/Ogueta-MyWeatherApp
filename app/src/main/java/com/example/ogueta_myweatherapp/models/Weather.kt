package com.example.ogueta_myweatherapp.models

data class Weather(
    val date: String, // 1 y 2
    val city: String, // 0
    val humidity: Int,//1 (rounded)
    val precipitationIntensity: Int,//1 (rounded)
    val precipitationProbability: Int, // 2
    val pressureSurfaceLevel: Int,//1 (rounded)
    val temperature: Int, //1 (rounded)
    val temperatureApparent: Int, //1 (rounded)
    val temperatureMax: Int, // 2 (rounded)
    val temperatureMin: Int, // 2 (rounded)
    val weatherCode: Int, //1 y 2
    val windDirection: Double, //1
    val windSpeed: Int, //1 (rounded)
)
