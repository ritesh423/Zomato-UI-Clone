package com.riteshapps.zomatoclone.data.local.dao

import androidx.room.*
import com.riteshapps.zomatoclone.data.local.entity.RestaurantEntity
import com.riteshapps.zomatoclone.data.local.entity.WishlistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WishlistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToWishlist(wishlist: WishlistEntity): Long

    @Delete
    suspend fun removeFromWishlist(wishlist: WishlistEntity)

    @Query("DELETE FROM wishlist WHERE restaurantId = :restaurantId")
    suspend fun removeFromWishlistByRestaurantId(restaurantId: Long)

    @Query("SELECT * FROM wishlist ORDER BY addedAt DESC")
    fun getAllWishlistItems(): Flow<List<WishlistEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM wishlist WHERE restaurantId = :restaurantId)")
    fun isInWishlist(restaurantId: Long): Flow<Boolean>

    @Query("SELECT EXISTS(SELECT 1 FROM wishlist WHERE restaurantId = :restaurantId)")
    suspend fun isInWishlistSync(restaurantId: Long): Boolean

    @Query("""
        SELECT r.* FROM restaurants r 
        INNER JOIN wishlist w ON r.id = w.restaurantId 
        ORDER BY w.addedAt DESC
    """)
    fun getWishlistRestaurants(): Flow<List<RestaurantEntity>>

    @Query("SELECT COUNT(*) FROM wishlist")
    fun getWishlistCount(): Flow<Int>

    @Query("DELETE FROM wishlist")
    suspend fun clearWishlist()
}
