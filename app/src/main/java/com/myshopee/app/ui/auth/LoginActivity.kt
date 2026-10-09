package com.myshopee.app.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.myshopee.app.data.local.SessionManager
import com.myshopee.app.data.repository.AuthRepository
import com.myshopee.app.databinding.ActivityLoginBinding
import com.myshopee.app.ui.main.MainActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sessionManager = SessionManager(this)
        authRepository = AuthRepository(sessionManager)

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val pass = binding.etPassword.text.toString().trim()

            val result = authRepository.loginUser(email, pass)
            result.onSuccess { user ->
                Toast.makeText(this, "Selamat datang, ${user.fullName}! 💗", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }.onFailure { ex ->
                Toast.makeText(this, ex.message ?: "Login gagal", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvRegisterLink.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
