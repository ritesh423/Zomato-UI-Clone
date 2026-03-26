package com.riteshapps.zomatoclone.data.local.dao

import androidx.room.*
import com.riteshapps.zomatoclone.data.local.entity.MenuItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MenuItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMenuItem(menuItem: MenuItemEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMenuItems(menuItems: List<MenuItemEntity>)

    @Update
    suspend fun updateMenuItem(menuItem: MenuItemEntity)

    @Delete
    suspend fun deleteMenuItem(menuItem: MenuItemEntity)

    @Query("SELECT * FROM menu_items WHERE id = :menuItemId")
    fun getMenuItemById(menuItemId: Long): Flow<MenuItemEntity?>

    @Query("SELECT * FROM menu_items WHERE id = :menuItemId")
    suspend fun getMenuItemByIdSync(menuItemId: Long): MenuItemEntity?

    @Query("SELECT * FROM menu_items WHERE restaurantId = :restaurantId ORDER BY category, name")
    fun getMenuItemsByRestaurant(restaurantId: Long): Flow<List<MenuItemEntity>>

    @Query("SELECT * FROM menu_items WHERE restaurantId = :restaurantId AND category = :category")
    fun getMenuItemsByCategory(restaurantId: Long, category: String): Flow<List<MenuItemEntity>>

    @Query("SELECT DISTINCT category FROM menu_items WHERE restaurantId = :restaurantId")
    fun getCategoriesForRestaurant(restaurantId: Long): Flow<List<String>>

    @Query("SELECT * FROM menu_items WHERE restaurantId = :restaurantId AND isVeg = 1")
    fun getVegMenuItems(restaurantId: Long): Flow<List<MenuItemEntity>>

    @Query("SELECT * FROM menu_items WHERE restaurantId = :restaurantId AND isBestSeller = 1")
    fun getBestSellers(restaurantId: Long): Flow<List<MenuItemEntity>>

    @Query("SELECT * FROM menu_items WHERE name LIKE '%' || :query || '%'")
    fun searchMenuItems(query: String): Flow<List<MenuItemEntity>>

    @Query("SELECT * FROM menu_items WHERE restaurantId = :restaurantId AND isAvailable = 1")
    fun getAvailableMenuItems(restaurantId: Long): Flow<List<MenuItemEntity>>
}
