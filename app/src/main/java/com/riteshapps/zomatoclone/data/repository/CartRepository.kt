package com.riteshapps.zomatoclone.data.repository

import com.riteshapps.zomatoclone.data.local.dao.CartDao
import com.riteshapps.zomatoclone.data.local.entity.CartItemEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartRepository @Inject constructor(
    private val cartDao: CartDao
) {
    fun getAllCartItems(): Flow<List<CartItemEntity>> {
        return cartDao.getAllCartItems()
    }

    fun getCartItemsByRestaurant(restaurantId: Long): Flow<List<CartItemEntity>> {
        return cartDao.getCartItemsByRestaurant(restaurantId)
    }

    suspend fun getCartItemByMenuId(menuItemId: Long): CartItemEntity? {
        return cartDao.getCartItemByMenuId(menuItemId)
    }

    fun getCartTotal(): Flow<Int?> {
        return cartDao.getCartTotal()
    }

    fun getCartItemCount(): Flow<Int?> {
        return cartDao.getCartItemCount()
    }

    fun getCartDistinctItemCount(): Flow<Int> {
        return cartDao.getCartDistinctItemCount()
    }

    suspend fun getCartRestaurantId(): Long? {
        return cartDao.getCartRestaurantId()
    }

    suspend fun addToCart(cartItem: CartItemEntity): Long {
        val existingItem = cartDao.getCartItemByMenuId(cartItem.menuItemId)
        return if (existingItem != null) {
            cartDao.updateQuantity(existingItem.id, existingItem.quantity + cartItem.quantity)
            existingItem.id
        } else {
            cartDao.insertCartItem(cartItem)
        }
    }

    suspend fun updateCartItem(cartItem: CartItemEntity) {
        cartDao.updateCartItem(cartItem)
    }

    suspend fun updateQuantity(cartItemId: Long, quantity: Int) {
        if (quantity <= 0) {
            cartDao.deleteCartItemById(cartItemId)
        } else {
            cartDao.updateQuantity(cartItemId, quantity)
        }
    }

    suspend fun removeFromCart(cartItem: CartItemEntity) {
        cartDao.deleteCartItem(cartItem)
    }

    suspend fun removeFromCartById(cartItemId: Long) {
        cartDao.deleteCartItemById(cartItemId)
    }

    suspend fun removeFromCartByMenuItemId(menuItemId: Long) {
        val item = cartDao.getCartItemByMenuId(menuItemId)
        if (item != null) {
            cartDao.deleteCartItemById(item.id)
        }
    }

    suspend fun clearCart() {
        cartDao.clearCart()
    }

    suspend fun incrementQuantity(menuItemId: Long) {
        val item = cartDao.getCartItemByMenuId(menuItemId)
        if (item != null) {
            cartDao.updateQuantity(item.id, item.quantity + 1)
        }
    }

    suspend fun decrementQuantity(menuItemId: Long) {
        val item = cartDao.getCartItemByMenuId(menuItemId)
        if (item != null) {
            if (item.quantity <= 1) {
                cartDao.deleteCartItemById(item.id)
            } else {
                cartDao.updateQuantity(item.id, item.quantity - 1)
            }
        }
    }
}
