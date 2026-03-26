package com.riteshapps.zomatoclone.data.local.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.riteshapps.zomatoclone.data.local.entity.RestaurantEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RestaurantDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRestaurant(restaurant: RestaurantEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRestaurants(restaurants: List<RestaurantEntity>)

    @Update
    suspend fun updateRestaurant(restaurant: RestaurantEntity)

    @Delete
    suspend fun deleteRestaurant(restaurant: RestaurantEntity)

    @Query("SELECT * FROM restaurants WHERE id = :restaurantId")
    fun getRestaurantById(restaurantId: Long): Flow<RestaurantEntity?>

    @Query("SELECT * FROM restaurants WHERE id = :restaurantId")
    suspend fun getRestaurantByIdSync(restaurantId: Long): RestaurantEntity?

    @Query("SELECT * FROM restaurants WHERE isOpen = 1 ORDER BY rating DESC")
    fun getAllOpenRestaurants(): Flow<List<RestaurantEntity>>

    @Query("SELECT * FROM restaurants ORDER BY rating DESC")
    fun getAllRestaurants(): Flow<List<RestaurantEntity>>

    @Query("SELECT * FROM restaurants ORDER BY rating DESC")
    fun getRestaurantsPaged(): PagingSource<Int, RestaurantEntity>

    @Query("SELECT * FROM restaurants WHERE categoryId = :categoryId ORDER BY rating DESC")
    fun getRestaurantsByCategory(categoryId: Long): Flow<List<RestaurantEntity>>

    @Query("SELECT * FROM restaurants WHERE name LIKE '%' || :query || '%' OR cuisine LIKE '%' || :query || '%'")
    fun searchRestaurants(query: String): Flow<List<RestaurantEntity>>

    @Query("SELECT * FROM restaurants WHERE isVegOnly = 1 ORDER BY rating DESC")
    fun getVegOnlyRestaurants(): Flow<List<RestaurantEntity>>

    @Query("SELECT * FROM restaurants WHERE rating >= :minRating ORDER BY rating DESC")
    fun getRestaurantsByMinRating(minRating: Float): Flow<List<RestaurantEntity>>

    @Query("SELECT * FROM restaurants ORDER BY rating DESC LIMIT :limit")
    fun getTopRatedRestaurants(limit: Int): Flow<List<RestaurantEntity>>

    @Query("SELECT COUNT(*) FROM restaurants")
    suspend fun getRestaurantCount(): Int
}
