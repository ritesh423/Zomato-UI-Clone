package com.riteshapps.zomatoclone.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riteshapps.zomatoclone.data.local.datastore.AuthDataStore
import com.riteshapps.zomatoclone.data.local.datastore.SettingsDataStore
import com.riteshapps.zomatoclone.data.local.entity.AddressEntity
import com.riteshapps.zomatoclone.data.local.entity.UserEntity
import com.riteshapps.zomatoclone.data.repository.AddressRepository
import com.riteshapps.zomatoclone.data.repository.AuthRepository
import com.riteshapps.zomatoclone.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val addressRepository: AddressRepository,
    private val authDataStore: AuthDataStore,
    private val settingsDataStore: SettingsDataStore
) : ViewModel() {

    val currentUser: StateFlow<UserEntity?> = authDataStore.userId
        .filterNotNull()
        .flatMapLatest { userRepository.getUserById(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val userName: StateFlow<String> = authRepository.currentUserName
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    val userEmail: StateFlow<String> = authRepository.currentUserEmail
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    val userPhone: StateFlow<String> = currentUser
        .map { it?.phone ?: "" }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    val isAdmin: StateFlow<Boolean> = authRepository.isAdmin
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val addresses: StateFlow<List<AddressEntity>> = authDataStore.userId
        .filterNotNull()
        .flatMapLatest { addressRepository.getAddressesByUser(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val darkMode: StateFlow<Boolean> = settingsDataStore.darkMode
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun toggleDarkMode() {
        viewModelScope.launch {
            val currentMode = darkMode.value
            settingsDataStore.setDarkMode(!currentMode)
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }

    fun updateProfile(name: String, phone: String) {
        viewModelScope.launch {
            val user = currentUser.value ?: return@launch
            val updatedUser = user.copy(name = name, phone = phone)
            authRepository.updateUserProfile(updatedUser)
        }
    }

    fun addAddress(label: String, fullAddress: String, landmark: String = "", isDefault: Boolean = false) {
        viewModelScope.launch {
            val userId = authDataStore.userId.first() ?: return@launch
            val address = AddressEntity(
                userId = userId,
                label = label,
                fullAddress = fullAddress,
                landmark = landmark,
                isDefault = isDefault
            )
            addressRepository.addAddress(address)
        }
    }

    fun setDefaultAddress(addressId: Long) {
        viewModelScope.launch {
            val userId = authDataStore.userId.first() ?: return@launch
            addressRepository.setAsDefault(userId, addressId)
        }
    }

    fun deleteAddress(address: AddressEntity) {
        viewModelScope.launch {
            addressRepository.deleteAddress(address)
        }
    }
}
