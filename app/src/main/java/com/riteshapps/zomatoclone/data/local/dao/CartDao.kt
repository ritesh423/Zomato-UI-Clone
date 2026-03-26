package com.riteshapps.zomatoclone.data.local.dao

import androidx.room.*
import com.riteshapps.zomatoclone.data.local.entity.CartItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(cartItem: CartItemEntity): Long

    @Update
    suspend fun updateCartItem(cartItem: CartItemEntity)

    @Delete
    suspend fun deleteCartItem(cartItem: CartItemEntity)

    @Query("DELETE FROM cart_items WHERE id = :cartItemId")
    suspend fun deleteCartItemById(cartItemId: Long)

    @Query("SELECT * FROM cart_items")
    fun getAllCartItems(): Flow<List<CartItemEntity>>

    @Query("SELECT * FROM cart_items WHERE restaurantId = :restaurantId")
    fun getCartItemsByRestaurant(restaurantId: Long): Flow<List<CartItemEntity>>

    @Query("SELECT * FROM cart_items WHERE menuItemId = :menuItemId LIMIT 1")
    suspend fun getCartItemByMenuId(menuItemId: Long): CartItemEntity?

    @Query("SELECT SUM(itemPrice * quantity) FROM cart_items")
    fun getCartTotal(): Flow<Int?>

    @Query("SELECT SUM(quantity) FROM cart_items")
    fun getCartItemCount(): Flow<Int?>

    @Query("SELECT COUNT(*) FROM cart_items")
    fun getCartDistinctItemCount(): Flow<Int>

    @Query("SELECT DISTINCT restaurantId FROM cart_items LIMIT 1")
    suspend fun getCartRestaurantId(): Long?

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()

    @Query("UPDATE cart_items SET quantity = :quantity WHERE id = :cartItemId")
    suspend fun updateQuantity(cartItemId: Long, quantity: Int)

    @Query("DELETE FROM cart_items WHERE quantity <= 0")
    suspend fun removeZeroQuantityItems()
}
