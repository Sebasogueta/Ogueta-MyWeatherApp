package com.example.ogueta_myweatherapp.models

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class WeatherViewModel: ViewModel() {

    private var firstDay: Int = 0
    private var selectedDay: Int = 0
    private var weatherInfoList: List<Weather> = listOf()

    fun setFirstDay(day: Int){
        firstDay = day
    }

    fun setDay(day: Int){
        selectedDay = day
    }

    fun getFirstDay():Int{
        return firstDay
    }

    fun getDay():Int{
        return selectedDay
    }

    fun getWeatherList():List<Weather>{
        return weatherInfoList
    }

    fun setWeatherList(mutableList: MutableList<Weather>){
        weatherInfoList = mutableList.toList()
    }

}
