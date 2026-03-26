package com.riteshapps.zomatoclone.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.riteshapps.zomatoclone.data.local.dao.*
import com.riteshapps.zomatoclone.data.local.entity.*

@Database(
    entities = [
        UserEntity::class,
        RestaurantEntity::class,
        MenuItemEntity::class,
        CategoryEntity::class,
        CartItemEntity::class,
        WishlistEntity::class,
        OrderEntity::class,
        OrderItemEntity::class,
        ReviewEntity::class,
        AddressEntity::class,
        BannerEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun restaurantDao(): RestaurantDao
    abstract fun menuItemDao(): MenuItemDao
    abstract fun categoryDao(): CategoryDao
    abstract fun cartDao(): CartDao
    abstract fun wishlistDao(): WishlistDao
    abstract fun orderDao(): OrderDao
    abstract fun reviewDao(): ReviewDao
    abstract fun addressDao(): AddressDao
    abstract fun bannerDao(): BannerDao

    companion object {
        const val DATABASE_NAME = "zomato_clone_db"
    }
}
