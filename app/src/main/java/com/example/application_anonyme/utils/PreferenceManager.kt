package com.example.application_anonyme.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.application_anonyme.data.model.User
import com.example.application_anonyme.data.api.User as ApiUser

class PreferenceManager(context: Context) {

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        Constants.PREFS_NAME,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveUser(user: User, token: String) {
        sharedPreferences.edit().apply {
            putString(Constants.KEY_USER_ID, user.id) // User.id is already a String
            putString(Constants.KEY_USER_PSEUDO, user.pseudo)
            putString(Constants.KEY_USER_TOKEN, token)
            putString(Constants.KEY_USER_CREATED_AT, user.createdAt)
            putBoolean(Constants.KEY_IS_LOGGED_IN, true)
            apply()
        }
    }
    
    fun saveUser(user: ApiUser, token: String) {
        sharedPreferences.edit().apply {
            putString(Constants.KEY_USER_ID, user.id.toString())
            putString(Constants.KEY_USER_PSEUDO, user.pseudo)
            putString(Constants.KEY_USER_TOKEN, token)
            putString(Constants.KEY_USER_CREATED_AT, user.createdAt)
            putBoolean(Constants.KEY_IS_LOGGED_IN, true)
            apply()
        }
    }

    fun getToken(): String? {
        return sharedPreferences.getString(Constants.KEY_USER_TOKEN, null)
    }

    fun getPseudo(): String? {
        return sharedPreferences.getString(Constants.KEY_USER_PSEUDO, null)
    }

    fun getUserId(): String? {
        return sharedPreferences.getString(Constants.KEY_USER_ID, null)
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(Constants.KEY_IS_LOGGED_IN, false)
    }

    fun getUser(): User? {
        val id = getUserId() ?: return null
        val pseudo = getPseudo() ?: return null
        val createdAt = sharedPreferences.getString(Constants.KEY_USER_CREATED_AT, "") ?: ""

        return User(id, pseudo, createdAt)
    }
    
    fun getUserPseudo(): String? = getPseudo()
    
    fun clearAll() {
        sharedPreferences.edit().clear().apply()
    }

    fun logout() {
        sharedPreferences.edit().clear().apply()
    }
}