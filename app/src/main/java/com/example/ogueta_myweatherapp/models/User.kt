package com.example.ogueta_myweatherapp.models

data class User(
    val username: String,
    val favorites: MutableList<String>
) {
    // Constructor sin argumentos (necesario para Firestore)
    constructor() : this("", mutableListOf())
}