package com.myshopee.app.data.repository

import com.myshopee.app.data.local.SessionManager
import com.myshopee.app.data.model.User
import java.util.UUID

class AuthRepository(private val sessionManager: SessionManager) {

    fun loginUser(email: String, pass: String): Result<User> {
        return if (email.contains("@") && pass.length >= 6) {
            val user = User(
                id = if (email == "diva@myshopee.app") "diva-demo-id" else UUID.randomUUID().toString(),
                fullName = if (email == "diva@myshopee.app") "Diva" else email.substringBefore("@").replaceFirstChar { it.uppercase() },
                email = email,
                phoneNumber = "081234567890",
                address = "Jl. Kartini No. 45, Kesambi, Kota Cirebon, Jawa Barat"
            )
            sessionManager.saveSession(user.id, user.fullName, user.email)
            Result.success(user)
        } else {
            Result.failure(Exception("Email atau password tidak valid! (Password min. 6 karakter)"))
        }
    }

    fun registerUser(fullName: String, email: String, pass: String): Result<User> {
        return if (fullName.isNotBlank() && email.contains("@") && pass.length >= 6) {
            val user = User(
                id = UUID.randomUUID().toString(),
                fullName = fullName,
                email = email,
                phoneNumber = "081234567890",
                address = "Kota Cirebon, Jawa Barat"
            )
            sessionManager.saveSession(user.id, user.fullName, user.email)
            Result.success(user)
        } else {
            Result.failure(Exception("Mohon isi seluruh data registrasi dengan benar."))
        }
    }

    fun logout() {
        sessionManager.clearSession()
    }
}
