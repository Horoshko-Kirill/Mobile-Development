package com.example.calculator.data.local

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class PassKeyManager(context: Context) {

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val prefs = EncryptedSharedPreferences.create(
        context,
        "passkey_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    companion object {
        private const val PASS_KEY = "pass_key"
    }

    fun isPassKeySet(): Boolean = prefs.contains(PASS_KEY)

    fun validatePassKey(input: String): Boolean {
        val stored = prefs.getString(PASS_KEY, null) ?: return false
        return input == stored
    }

    fun setPassKey(input: String) {
        prefs.edit().putString(PASS_KEY, input).apply()
    }

    fun resetPassKey() {
        prefs.edit().remove(PASS_KEY).apply()
    }
}
