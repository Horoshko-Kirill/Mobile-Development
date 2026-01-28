package com.example.calculator.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class HistoryRepository {

    private val firestore by lazy {
        FirebaseFirestore.getInstance()
    }
    private val collection = firestore.collection("calculator_history")

    suspend fun saveEntry(userId: String, entry: String) {
        try {
            val data = mapOf(
                "entry" to entry,
                "timestamp" to System.currentTimeMillis()
            )
            collection.document(userId).collection("entries").add(data).await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun getHistory(userId: String): List<String> {
        return try {
            val snapshot = collection.document(userId).collection("entries")
                .orderBy("timestamp")
                .get()
                .await()
            snapshot.documents.mapNotNull { it.getString("entry") }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
