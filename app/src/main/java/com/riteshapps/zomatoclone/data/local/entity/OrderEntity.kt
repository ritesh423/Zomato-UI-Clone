package com.riteshapps.zomatoclone.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true)
    val orderId: Long = 0,
    val userId: Long,
    val restaurantId: Long,
    val restaurantName: String,
    val totalAmount: Int,
    val deliveryFee: Int = 0,
    val gstAmount: Int = 0,
    val discount: Int = 0,
    val status: String, // "Pending", "Confirmed", "Preparing", "Out for Delivery", "Delivered", "Cancelled"
    val timestamp: Long = System.currentTimeMillis(),
    val deliveryAddress: String = "",
    val paymentMethod: String = "COD"
)

@Entity(
    tableName = "order_items",
    foreignKeys = [
        ForeignKey(
            entity = OrderEntity::class,
            parentColumns = ["orderId"],
            childColumns = ["orderId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("orderId")]
)
data class OrderItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val orderId: Long,
    val itemName: String,
    val itemPrice: Int,
    val quantity: Int,
    val isVeg: Boolean = true
)
