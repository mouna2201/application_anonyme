package com.example.application_anonyme.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.application_anonyme.data.api.User
import com.example.application_anonyme.data.repository.UserRepository
import com.example.application_anonyme.utils.NetworkResult
import kotlinx.coroutines.launch

sealed class LoginState {
    object Loading : LoginState()
    data class Success(val user: User, val token: String) : LoginState()
    data class Error(val message: String) : LoginState()
}

class LoginViewModel : ViewModel() {

    private val repository = UserRepository()

    private val _loginState = MutableLiveData<LoginState>()
    val loginState: LiveData<LoginState> = _loginState

    fun login(pseudo: String, password: String = "") {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading

            val result = repository.login(pseudo, password)

            _loginState.value = when (result) {
                is NetworkResult.Success -> {
                    result.data?.let {
                        LoginState.Success(it.user, it.token)
                    } ?: LoginState.Error("Erreur lors de la connexion")
                }
                is NetworkResult.Error -> {
                    LoginState.Error(result.message ?: "Erreur inconnue")
                }
                is NetworkResult.Loading -> LoginState.Loading
            }
        }
    }
}