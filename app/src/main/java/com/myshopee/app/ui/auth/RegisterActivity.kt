package com.myshopee.app.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.myshopee.app.data.local.SessionManager
import com.myshopee.app.data.repository.AuthRepository
import com.myshopee.app.databinding.ActivityRegisterBinding
import com.myshopee.app.ui.main.MainActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sessionManager = SessionManager(this)
        authRepository = AuthRepository(sessionManager)

        binding.btnRegister.setOnClickListener {
            val name = binding.etRegFullName.text.toString().trim()
            val email = binding.etRegEmail.text.toString().trim()
            val pass = binding.etRegPassword.text.toString().trim()
            val confirm = binding.etRegConfirmPassword.text.toString().trim()

            if (pass != confirm) {
                Toast.makeText(this, "Password dan konfirmasi tidak cocok!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val result = authRepository.registerUser(name, email, pass)
            result.onSuccess { user ->
                Toast.makeText(this, "Registrasi Berhasil! Selamat datang, ${user.fullName} 💗", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finishAffinity()
            }.onFailure { ex ->
                Toast.makeText(this, ex.message ?: "Registrasi gagal", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvLoginLink.setOnClickListener {
            finish()
        }
    }
}
