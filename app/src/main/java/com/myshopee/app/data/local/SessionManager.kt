package com.myshopee.app.data.local

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREF_NAME = "MyShopeeSession"
        private const val KEY_IS_LOGGED_IN = "isLoggedIn"
        private const val KEY_USER_ID = "userId"
        private const val KEY_USER_NAME = "userName"
        private const val KEY_USER_EMAIL = "userEmail"
        private const val KEY_AUTH_TOKEN = "authToken"
    }

    fun saveSession(userId: String, name: String, email: String, token: String = "") {
        prefs.edit().apply {
            putBoolean(KEY_IS_LOGGED_IN, true)
            putString(KEY_USER_ID, userId)
            putString(KEY_USER_NAME, name)
            putString(KEY_USER_EMAIL, email)
            putString(KEY_AUTH_TOKEN, token)
            apply()
        }
    }

    fun isLoggedIn(): Boolean = prefs.getBoolean(KEY_IS_LOGGED_IN, false)

    fun getUserName(): String = prefs.getString(KEY_USER_NAME, "Diva") ?: "Diva"

    fun getUserEmail(): String = prefs.getString(KEY_USER_EMAIL, "diva@myshopee.app") ?: "diva@myshopee.app"

    fun getUserId(): String = prefs.getString(KEY_USER_ID, "demo-diva-id") ?: "demo-diva-id"

    fun getAuthToken(): String = prefs.getString(KEY_AUTH_TOKEN, "") ?: ""

    fun clearSession() {
        prefs.edit().clear().apply()
    }
}
