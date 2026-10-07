package com.example.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.SettingsRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface LoginUiState {
    data object Idle : LoginUiState
    data object Loading : LoginUiState
    data class Success(val userName: String) : LoginUiState
    data class Error(val message: String) : LoginUiState
}

class LoginViewModel(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun loginWithGoogle(accountName: String = "Google User") {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            delay(600) // Smooth authentication transition
            val email = "${accountName.lowercase().replace(" ", ".")}@gmail.com"
            settingsRepository.login(
                name = accountName,
                email = email,
                provider = "GOOGLE"
            )
            _uiState.value = LoginUiState.Success(accountName)
        }
    }

    fun loginWithFacebook() {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            delay(600)
            val name = "Facebook User"
            val email = "user@facebook.com"
            settingsRepository.login(
                name = name,
                email = email,
                provider = "FACEBOOK"
            )
            _uiState.value = LoginUiState.Success(name)
        }
    }

    fun loginWithNasadef(idOrEmail: String, pass: String) {
        val trimmedId = idOrEmail.trim()
        val trimmedPass = pass.trim()

        if (trimmedId.isEmpty()) {
            _uiState.value = LoginUiState.Error("Sila masukkan ID atau Emel Nasadef anda.")
            return
        }

        if (trimmedPass.isEmpty()) {
            _uiState.value = LoginUiState.Error("Sila masukkan kata laluan akaun Nasadef.")
            return
        }

        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            delay(700)
            val displayName = if (trimmedId.contains("@")) {
                trimmedId.substringBefore("@").replaceFirstChar { it.uppercase() }
            } else {
                trimmedId.replaceFirstChar { it.uppercase() }
            }

            val email = if (trimmedId.contains("@")) trimmedId else "$trimmedId@nasadef.com.my"

            settingsRepository.login(
                name = displayName,
                email = email,
                provider = "NASADEF"
            )
            _uiState.value = LoginUiState.Success(displayName)
        }
    }

    fun resetState() {
        _uiState.value = LoginUiState.Idle
    }
}
