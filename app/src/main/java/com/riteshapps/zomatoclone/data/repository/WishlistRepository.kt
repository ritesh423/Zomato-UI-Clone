package com.riteshapps.zomatoclone.data.repository

import com.riteshapps.zomatoclone.data.local.dao.WishlistDao
import com.riteshapps.zomatoclone.data.local.entity.RestaurantEntity
import com.riteshapps.zomatoclone.data.local.entity.WishlistEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WishlistRepository @Inject constructor(
    private val wishlistDao: WishlistDao
) {
    fun getAllWishlistItems(): Flow<List<WishlistEntity>> {
        return wishlistDao.getAllWishlistItems()
    }

    fun getWishlistRestaurants(): Flow<List<RestaurantEntity>> {
        return wishlistDao.getWishlistRestaurants()
    }

    fun isInWishlist(restaurantId: Long): Flow<Boolean> {
        return wishlistDao.isInWishlist(restaurantId)
    }

    suspend fun isInWishlistSync(restaurantId: Long): Boolean {
        return wishlistDao.isInWishlistSync(restaurantId)
    }

    fun getWishlistCount(): Flow<Int> {
        return wishlistDao.getWishlistCount()
    }

    suspend fun addToWishlist(restaurantId: Long) {
        val wishlistItem = WishlistEntity(restaurantId = restaurantId)
        wishlistDao.addToWishlist(wishlistItem)
    }

    suspend fun removeFromWishlist(restaurantId: Long) {
        wishlistDao.removeFromWishlistByRestaurantId(restaurantId)
    }

    suspend fun toggleWishlist(restaurantId: Long) {
        if (wishlistDao.isInWishlistSync(restaurantId)) {
            wishlistDao.removeFromWishlistByRestaurantId(restaurantId)
        } else {
            wishlistDao.addToWishlist(WishlistEntity(restaurantId = restaurantId))
        }
    }

    suspend fun clearWishlist() {
        wishlistDao.clearWishlist()
    }
}
