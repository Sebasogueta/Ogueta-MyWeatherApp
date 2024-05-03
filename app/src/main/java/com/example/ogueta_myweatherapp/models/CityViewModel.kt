package com.example.ogueta_myweatherapp.models

import androidx.lifecycle.ViewModel

class CityViewModel : ViewModel() {

    private var city: String = ""

    fun setCity(newCity:String){
        city = newCity
    }

    fun getCity():String{
        return city
    }

}