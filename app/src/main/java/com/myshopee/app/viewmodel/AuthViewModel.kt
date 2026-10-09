package com.myshopee.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.myshopee.app.data.model.User
import com.myshopee.app.data.repository.AuthRepository

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _loginState = MutableLiveData<Result<User>>()
    val loginState: LiveData<Result<User>> = _loginState

    private val _registerState = MutableLiveData<Result<User>>()
    val registerState: LiveData<Result<User>> = _registerState

    fun login(email: String, pass: String) {
        val res = authRepository.loginUser(email, pass)
        _loginState.value = res
    }

    fun register(fullName: String, email: String, pass: String) {
        val res = authRepository.registerUser(fullName, email, pass)
        _registerState.value = res
    }
}
