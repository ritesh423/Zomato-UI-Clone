package com.riteshapps.zomatoclone.data.repository

import com.riteshapps.zomatoclone.data.local.dao.OrderDao
import com.riteshapps.zomatoclone.data.local.entity.CartItemEntity
import com.riteshapps.zomatoclone.data.local.entity.OrderEntity
import com.riteshapps.zomatoclone.data.local.entity.OrderItemEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRepository @Inject constructor(
    private val orderDao: OrderDao
) {
    fun getAllOrders(): Flow<List<OrderEntity>> {
        return orderDao.getAllOrders()
    }

    fun getOrdersByUser(userId: Long): Flow<List<OrderEntity>> {
        return orderDao.getOrdersByUser(userId)
    }

    fun getOrderById(orderId: Long): Flow<OrderEntity?> {
        return orderDao.getOrderById(orderId)
    }

    fun getOrderItems(orderId: Long): Flow<List<OrderItemEntity>> {
        return orderDao.getOrderItems(orderId)
    }

    fun getOrdersByStatus(status: String): Flow<List<OrderEntity>> {
        return orderDao.getOrdersByStatus(status)
    }

    fun getRecentOrders(userId: Long, limit: Int = 5): Flow<List<OrderEntity>> {
        return orderDao.getRecentOrders(userId, limit)
    }

    fun getTotalRevenue(): Flow<Int?> {
        return orderDao.getTotalRevenue()
    }

    suspend fun createOrder(
        userId: Long,
        restaurantId: Long,
        restaurantName: String,
        cartItems: List<CartItemEntity>,
        deliveryFee: Int,
        discount: Int,
        deliveryAddress: String,
        paymentMethod: String
    ): Long {
        val itemTotal = cartItems.sumOf { it.itemPrice * it.quantity }
        val gstAmount = (itemTotal * 0.05).toInt()
        val totalAmount = itemTotal + deliveryFee + gstAmount - discount

        val order = OrderEntity(
            userId = userId,
            restaurantId = restaurantId,
            restaurantName = restaurantName,
            totalAmount = totalAmount,
            deliveryFee = deliveryFee,
            gstAmount = gstAmount,
            discount = discount,
            status = "Confirmed",
            deliveryAddress = deliveryAddress,
            paymentMethod = paymentMethod
        )

        val orderItems = cartItems.map { cartItem ->
            OrderItemEntity(
                orderId = 0, // Will be set by DAO
                itemName = cartItem.itemName,
                itemPrice = cartItem.itemPrice,
                quantity = cartItem.quantity,
                isVeg = cartItem.isVeg
            )
        }

        return orderDao.createOrderWithItems(order, orderItems)
    }

    suspend fun updateOrderStatus(orderId: Long, status: String) {
        orderDao.updateOrderStatus(orderId, status)
    }

    suspend fun getOrderCount(): Int {
        return orderDao.getOrderCount()
    }
}
