package com.riteshapps.zomatoclone.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riteshapps.zomatoclone.common.UiState
import com.riteshapps.zomatoclone.data.local.datastore.AuthDataStore
import com.riteshapps.zomatoclone.data.local.entity.AddressEntity
import com.riteshapps.zomatoclone.data.local.entity.CartItemEntity
import com.riteshapps.zomatoclone.data.repository.AddressRepository
import com.riteshapps.zomatoclone.data.repository.CartRepository
import com.riteshapps.zomatoclone.data.repository.OrderRepository
import com.riteshapps.zomatoclone.data.repository.RestaurantRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val orderRepository: OrderRepository,
    private val addressRepository: AddressRepository,
    private val restaurantRepository: RestaurantRepository,
    private val authDataStore: AuthDataStore
) : ViewModel() {

    private val _userId = MutableStateFlow<Long?>(null)

    val addresses: StateFlow<List<AddressEntity>> = _userId
        .filterNotNull()
        .flatMapLatest { addressRepository.getAddressesByUser(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedAddressId = MutableStateFlow<Long?>(null)
    val selectedAddressId: StateFlow<Long?> = _selectedAddressId.asStateFlow()

    private val _selectedPaymentMethod = MutableStateFlow("COD")
    val selectedPaymentMethod: StateFlow<String> = _selectedPaymentMethod.asStateFlow()

    private val _orderState = MutableStateFlow<UiState<Long>>(UiState.Idle)
    val orderState: StateFlow<UiState<Long>> = _orderState.asStateFlow()

    init {
        viewModelScope.launch {
            authDataStore.userId.collect { userId ->
                _userId.value = userId
                if (userId != null) {
                    val defaultAddress = addressRepository.getDefaultAddressSync(userId)
                    _selectedAddressId.value = defaultAddress?.id
                }
            }
        }
    }

    fun selectAddress(addressId: Long) {
        _selectedAddressId.value = addressId
    }

    fun selectPaymentMethod(method: String) {
        _selectedPaymentMethod.value = method
    }

    fun placeOrder(
        cartItems: List<CartItemEntity>,
        restaurantId: Long,
        restaurantName: String,
        deliveryFee: Int,
        discount: Int
    ) {
        val userId = _userId.value
        val addressId = _selectedAddressId.value

        if (userId == null) {
            _orderState.value = UiState.Error("Please login to place order")
            return
        }

        if (addressId == null) {
            _orderState.value = UiState.Error("Please select a delivery address")
            return
        }

        if (cartItems.isEmpty()) {
            _orderState.value = UiState.Error("Your cart is empty")
            return
        }

        viewModelScope.launch {
            _orderState.value = UiState.Loading
            try {
                val address = addresses.value.find { it.id == addressId }
                val orderId = orderRepository.createOrder(
                    userId = userId,
                    restaurantId = restaurantId,
                    restaurantName = restaurantName,
                    cartItems = cartItems,
                    deliveryFee = deliveryFee,
                    discount = discount,
                    deliveryAddress = address?.fullAddress ?: "",
                    paymentMethod = _selectedPaymentMethod.value
                )
                cartRepository.clearCart()
                _orderState.value = UiState.Success(orderId)
            } catch (e: Exception) {
                _orderState.value = UiState.Error(e.message ?: "Failed to place order")
            }
        }
    }

    fun addAddress(label: String, fullAddress: String, landmark: String) {
        val userId = _userId.value ?: return
        viewModelScope.launch {
            val isFirst = addresses.value.isEmpty()
            val address = AddressEntity(
                userId = userId,
                label = label,
                fullAddress = fullAddress,
                landmark = landmark,
                isDefault = isFirst
            )
            val addressId = addressRepository.addAddress(address)
            if (isFirst) {
                _selectedAddressId.value = addressId
            }
        }
    }

    fun resetOrderState() {
        _orderState.value = UiState.Idle
    }
}
