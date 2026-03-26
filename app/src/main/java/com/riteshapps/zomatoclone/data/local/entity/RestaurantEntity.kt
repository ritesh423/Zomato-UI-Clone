package com.riteshapps.zomatoclone.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "restaurants")
data class RestaurantEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val cuisine: String,
    val rating: Float,
    val ratingCount: Int = 0,
    val deliveryTime: String,
    val minOrder: Int = 0,
    val deliveryFee: Int = 0,
    val distance: String = "",
    val imageResId: Int,
    val isOpen: Boolean = true,
    val categoryId: Long,
    val offerTag: String = "",
    val address: String = "",
    val isVegOnly: Boolean = false
)
