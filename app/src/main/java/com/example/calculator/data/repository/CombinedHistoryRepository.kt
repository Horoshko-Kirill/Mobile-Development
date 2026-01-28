package com.example.calculator.data.repository

import com.example.calculator.data.local.LocalStorage
import com.example.calculator.data.remote.HistoryRepository

class CombinedHistoryRepository(
    private val localStorage: LocalStorage,
    private val remoteStorage: HistoryRepository
) {

    suspend fun saveEntry(userId: String, entry: String) {
        localStorage.saveEntry(entry)
        remoteStorage.saveEntry(userId, entry)
    }

    suspend fun getHistory(userId: String): List<String> {
        val local = localStorage.getHistory()
        val remote = remoteStorage.getHistory(userId)
        return (local + remote).distinct()
    }
}
