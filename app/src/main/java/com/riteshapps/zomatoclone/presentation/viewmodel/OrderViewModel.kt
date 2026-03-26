package com.riteshapps.zomatoclone.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riteshapps.zomatoclone.data.local.datastore.AuthDataStore
import com.riteshapps.zomatoclone.data.local.entity.OrderEntity
import com.riteshapps.zomatoclone.data.local.entity.OrderItemEntity
import com.riteshapps.zomatoclone.data.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
    private val authDataStore: AuthDataStore
) : ViewModel() {

    private val _userId = authDataStore.userId
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val orders: StateFlow<List<OrderEntity>> = _userId
        .filterNotNull()
        .flatMapLatest { orderRepository.getOrdersByUser(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val recentOrders: StateFlow<List<OrderEntity>> = _userId
        .filterNotNull()
        .flatMapLatest { orderRepository.getRecentOrders(it, 5) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun getOrderItems(orderId: Long): Flow<List<OrderItemEntity>> {
        return orderRepository.getOrderItems(orderId)
    }

    fun getOrderById(orderId: Long): Flow<OrderEntity?> {
        return orderRepository.getOrderById(orderId)
    }
}
