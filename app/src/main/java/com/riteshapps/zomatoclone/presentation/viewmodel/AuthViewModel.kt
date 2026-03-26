package com.riteshapps.zomatoclone.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riteshapps.zomatoclone.common.UiState
import com.riteshapps.zomatoclone.data.local.datastore.SettingsDataStore
import com.riteshapps.zomatoclone.data.local.entity.UserEntity
import com.riteshapps.zomatoclone.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val settingsDataStore: SettingsDataStore
) : ViewModel() {

    private val _loginState = MutableStateFlow<UiState<UserEntity>>(UiState.Idle)
    val loginState: StateFlow<UiState<UserEntity>> = _loginState.asStateFlow()

    private val _registerState = MutableStateFlow<UiState<UserEntity>>(UiState.Idle)
    val registerState: StateFlow<UiState<UserEntity>> = _registerState.asStateFlow()

    val isLoggedIn: StateFlow<Boolean> = authRepository.isLoggedIn
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val currentUserId: StateFlow<Long?> = authRepository.currentUserId
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val currentUserName: StateFlow<String> = authRepository.currentUserName
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    val isAdmin: StateFlow<Boolean> = authRepository.isAdmin
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val onboardingShown: StateFlow<Boolean> = settingsDataStore.onboardingShown
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun setOnboardingShown() {
        viewModelScope.launch {
            settingsDataStore.setOnboardingShown(true)
        }
    }

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _loginState.value = UiState.Error("Please fill all fields")
            return
        }

        viewModelScope.launch {
            _loginState.value = UiState.Loading
            val result = authRepository.login(email, password)
            _loginState.value = result.fold(
                onSuccess = { UiState.Success(it) },
                onFailure = { UiState.Error(it.message ?: "Login failed") }
            )
        }
    }

    fun register(name: String, email: String, password: String, confirmPassword: String) {
        when {
            name.isBlank() || email.isBlank() || password.isBlank() -> {
                _registerState.value = UiState.Error("Please fill all fields")
                return
            }
            password != confirmPassword -> {
                _registerState.value = UiState.Error("Passwords do not match")
                return
            }
            password.length < 6 -> {
                _registerState.value = UiState.Error("Password must be at least 6 characters")
                return
            }
            !email.contains("@") -> {
                _registerState.value = UiState.Error("Please enter a valid email")
                return
            }
        }

        viewModelScope.launch {
            _registerState.value = UiState.Loading
            val result = authRepository.register(name, email, password)
            _registerState.value = result.fold(
                onSuccess = { UiState.Success(it) },
                onFailure = { UiState.Error(it.message ?: "Registration failed") }
            )
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _loginState.value = UiState.Idle
        }
    }

    fun resetLoginState() {
        _loginState.value = UiState.Idle
    }

    fun resetRegisterState() {
        _registerState.value = UiState.Idle
    }
}
