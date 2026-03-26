package com.riteshapps.zomatoclone.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "banners")
data class BannerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val imageResId: Int,
    val title: String = "",
    val subtitle: String = "",
    val offerText: String = "",
    val targetScreen: String = "" // Route name to navigate to
)
