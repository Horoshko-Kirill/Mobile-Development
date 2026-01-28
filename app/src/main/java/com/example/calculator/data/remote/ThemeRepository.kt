package com.example.calculator.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

data class ThemeData(
    val primaryColor: String = "#6200EE",
    val secondaryColor: String = "#03DAC5",
    val tertiaryColor: String = "#BB86FC"
)

class ThemeRepository {
    private val firestore by lazy { FirebaseFirestore.getInstance() }
    private val collection = firestore.collection("calculator_themes")

    suspend fun saveTheme(userId: String, themeType: String, theme: ThemeData) {
        collection.document(userId).collection("themes").document(themeType).set(theme).await()
    }

    suspend fun loadTheme(userId: String, themeType: String): ThemeData {
        return try {
            val snapshot = collection.document(userId).collection("themes").document(themeType).get().await()
            snapshot.toObject(ThemeData::class.java) ?: ThemeData()
        } catch (e: Exception) {
            ThemeData()
        }
    }

    suspend fun loadAllThemes(userId: String): Pair<ThemeData, ThemeData> {
        val light = loadTheme(userId, "light")
        val dark = loadTheme(userId, "dark")
        return light to dark
    }
}