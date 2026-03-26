package com.riteshapps.zomatoclone.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "menu_items",
    foreignKeys = [
        ForeignKey(
            entity = RestaurantEntity::class,
            parentColumns = ["id"],
            childColumns = ["restaurantId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("restaurantId")]
)
data class MenuItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val restaurantId: Long,
    val name: String,
    val description: String,
    val price: Int,
    val imageResId: Int,
    val isVeg: Boolean,
    val category: String,
    val rating: Float = 0f,
    val isBestSeller: Boolean = false,
    val isAvailable: Boolean = true
)
