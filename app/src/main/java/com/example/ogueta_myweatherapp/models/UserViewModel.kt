package com.example.ogueta_myweatherapp.models

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class UserViewModel: ViewModel() {
    val auth: FirebaseAuth = Firebase.auth

    fun signWithEmailAndPassword(email: String, password: String, context: Context, result: (Boolean) -> Unit) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                result(true)
                Toast.makeText(
                    context,
                    "You have successfully logged in!",
                    Toast.LENGTH_SHORT
                ).show()

            } else {
                result(false)
                Toast.makeText(
                    context,
                    "Error: ${task.exception?.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    fun registerWithEmailAndPassword(username: String, email: String, password: String, context: Context, result: (Boolean) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val usersRef = db.collection("users")

        // Verificar si el usuario ya existe en la base de datos
        checkUserExistence(username) { userExists ->
            if (!userExists) { // if username is not used
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        val currentUser = auth.currentUser
                        val newUser = User(username, mutableListOf())

                        // Verificar si el usuario ya tiene un documento en Firestore
                        currentUser?.uid?.let { userId ->
                            val userDocument = usersRef.document(userId)
                            userDocument.get().addOnSuccessListener { documentSnapshot ->
                                if (documentSnapshot.exists()) {
                                    // El documento ya existe, no se puede crear de nuevo
                                    result(false)
                                } else {
                                    // El documento no existe, se crea y se guardan los datos
                                    userDocument.set(newUser)
                                        .addOnSuccessListener {
                                            Log.d(
                                                "Firestore",
                                                "Document $userId created with user data: $newUser"
                                            )
                                            result(true)
                                        }
                                        .addOnFailureListener { e ->
                                            Log.e(
                                                "Firestore",
                                                "Error creating document $userId: $e"
                                            )
                                            result(false)
                                        }
                                }
                            }.addOnFailureListener { e ->
                                Log.e("Firestore", "Error checking document existence: $e")
                                result(false)
                            }
                        } ?: run {
                            Log.e("Auth", "Current user is null")
                            result(false)
                        }
                    } else {
                        result(false)
                        Toast.makeText(
                            context,
                            "Error: ${task.exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
            } else {
                result(false) //user already exists
                Toast.makeText(
                    context,
                    "Error: username already exists",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun checkUserExistence(username: String, result: (Boolean) -> Unit) {

        val db = FirebaseFirestore.getInstance()
        var result2 = false

        db.collection("users").get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val usernameList = ArrayList<String>()
                for (document in task.result) {
                    val userData = document.toObject(User::class.java)
                    userData?.let { user ->
                        if (username.equals(user.username)) {
                            result2 = true //user already exists
                        }
                    }
                }
                result(result2)
            } else {
                result(true) //error
            }
        }

    }

    fun getFavoriteCities(callback: (MutableList<String>) -> Unit) {
        val currentUser = this.auth.currentUser
        currentUser?.let { firebaseUser ->
            val userId = firebaseUser.uid
            val db = FirebaseFirestore.getInstance()
            val usersRef = db.collection("users")
            val userDocument = usersRef.document(userId)
            var favCities = mutableListOf<String>()


            userDocument.get().addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val userData = documentSnapshot.toObject(User::class.java)
                    userData?.let { user ->
                        favCities = user.favorites
                        callback(favCities)
                        // Actualización del estado de favoriteCities cuando se completa la operación
                    }
                } else {
                    // El documento del usuario no existe
                }
                callback.invoke(favCities)
            }.addOnFailureListener { e ->
                // Manejo de errores al obtener el documento del usuario
            }
        }
    }

    fun removeFavorite(favoriteCities: MutableList<String>, favoriteCity: String, callback: (MutableList<String>) -> Unit){

    val db = FirebaseFirestore.getInstance()
    val currentUser = auth.currentUser

    if (currentUser != null) {
        val usersRef = db.collection("users")
        val userDocumentRef = usersRef.document(currentUser.uid)

        userDocumentRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val favoritesList = documentSnapshot.data!!["favorites"] as MutableList<String>
                    favoritesList.remove(favoriteCity)

                    val updatedMap = hashMapOf<String, Any>()
                    updatedMap["favorites"] = favoritesList

                    userDocumentRef.update(updatedMap)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d("Firestore", "Favorites updated successfully")
                                callback(favoritesList)
                            } else {
                                Log.e("Firestore", "Error updating favorites: ${task.exception}")
                                callback(favoritesList)
                            }
                        }
                }
            }
    }
}

    fun addFavorite(favoriteCities: MutableList<String>, favoriteCity: String, callback: (MutableList<String>) -> Unit){

        val db = FirebaseFirestore.getInstance()
        val currentUser = auth.currentUser

        if (currentUser != null) {
            val usersRef = db.collection("users")
            val userDocumentRef = usersRef.document(currentUser.uid)

            userDocumentRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val favoritesList = documentSnapshot.data!!["favorites"] as MutableList<String>
                        favoritesList.add(favoriteCity)

                        val updatedMap = hashMapOf<String, Any>()
                        updatedMap["favorites"] = favoritesList

                        userDocumentRef.update(updatedMap)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Log.d("Firestore", "Favorites updated successfully")
                                    callback(favoritesList)
                                } else {
                                    Log.e("Firestore", "Error updating favorites: ${task.exception}")
                                    callback(favoritesList)
                                }
                            }
                    }
                }
        }
    }

    fun getUser(callback: (String) -> Unit){

        val currentUser = this.auth.currentUser
        currentUser?.let { firebaseUser ->
            val userId = firebaseUser.uid
            val db = FirebaseFirestore.getInstance()
            val usersRef = db.collection("users")
            val userDocument = usersRef.document(userId)
            var username = "User"


            userDocument.get().addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val userData = documentSnapshot.toObject(User::class.java)
                    userData?.let { user ->
                        username = user.username
                        callback(username)

                    }
                } else {
                    username = "User"
                    callback(username)
                }
                callback.invoke(username)
            }.addOnFailureListener { e ->
                // Manejo de errores al obtener el documento del usuario
            }
        }

    }


}


