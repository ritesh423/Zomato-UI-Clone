package com.riteshapps.zomatoclone.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val email: String,
    val passwordHash: String,
    val phone: String = "",
    val avatarResId: Int = 0,
    val isAdmin: Boolean = false
)
