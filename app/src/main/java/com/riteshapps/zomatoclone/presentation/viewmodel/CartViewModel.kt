package com.riteshapps.zomatoclone.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riteshapps.zomatoclone.data.local.entity.CartItemEntity
import com.riteshapps.zomatoclone.data.local.entity.MenuItemEntity
import com.riteshapps.zomatoclone.data.repository.CartRepository
import com.riteshapps.zomatoclone.data.repository.RestaurantRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val restaurantRepository: RestaurantRepository
) : ViewModel() {

    val cartItems: StateFlow<List<CartItemEntity>> = cartRepository.getAllCartItems()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val cartTotal: StateFlow<Int> = cartRepository.getCartTotal()
        .map { it ?: 0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // Alias for cartTotal - used in some screens
    val itemTotal: StateFlow<Int> = cartTotal

    val cartItemCount: StateFlow<Int> = cartRepository.getCartItemCount()
        .map { it ?: 0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // Alias for cartItemCount - used in some screens
    val itemCount: StateFlow<Int> = cartItemCount

    val cartDistinctItemCount: StateFlow<Int> = cartRepository.getCartDistinctItemCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    private val _cartRestaurantId = MutableStateFlow<Long?>(null)
    val cartRestaurantId: StateFlow<Long?> = _cartRestaurantId.asStateFlow()

    private val _restaurantName = MutableStateFlow("")
    val restaurantName: StateFlow<String> = _restaurantName.asStateFlow()

    private val _appliedCoupon = MutableStateFlow<CouponInfo?>(null)
    val appliedCoupon: StateFlow<String?> = _appliedCoupon.map { it?.code }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val deliveryFee: StateFlow<Int> = cartTotal.map { total ->
        when {
            total >= 499 -> 0
            total >= 299 -> 20
            else -> 30
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 30)

    val gst: StateFlow<Int> = cartTotal.map { (it * 0.05).toInt() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val discount: StateFlow<Int> = combine(cartTotal, _appliedCoupon) { total, coupon ->
        when (coupon?.code) {
            "SAVE10" -> (total * 0.10).toInt()
            "FIRST50" -> minOf(50, total)
            "FREEDEL" -> 0
            else -> 0
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val grandTotal: StateFlow<Int> = combine(
        cartTotal, deliveryFee, gst, discount, _appliedCoupon
    ) { total, delivery, gstAmt, discountAmt, coupon ->
        val finalDelivery = if (coupon?.code == "FREEDEL") 0 else delivery
        total + finalDelivery + gstAmt - discountAmt
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    init {
        viewModelScope.launch {
            _cartRestaurantId.value = cartRepository.getCartRestaurantId()
            _cartRestaurantId.value?.let { restaurantId ->
                restaurantRepository.getRestaurantById(restaurantId).collect { restaurant ->
                    _restaurantName.value = restaurant?.name ?: ""
                }
            }
        }
    }

    fun addItem(menuItem: MenuItemEntity, restaurantId: Long, restaurantName: String) {
        viewModelScope.launch {
            val currentRestaurantId = cartRepository.getCartRestaurantId()
            
            // If cart has items from different restaurant, clear first
            if (currentRestaurantId != null && currentRestaurantId != restaurantId) {
                cartRepository.clearCart()
            }

            val cartItem = CartItemEntity(
                menuItemId = menuItem.id,
                restaurantId = restaurantId,
                itemName = menuItem.name,
                itemPrice = menuItem.price,
                imageResId = menuItem.imageResId,
                quantity = 1,
                isVeg = menuItem.isVeg
            )
            cartRepository.addToCart(cartItem)
            _cartRestaurantId.value = restaurantId
            _restaurantName.value = restaurantName
        }
    }

    fun addToCart(menuItem: MenuItemEntity, restaurantId: Long) {
        addItem(menuItem, restaurantId, "")
    }

    fun incrementItem(menuItemId: Long) {
        viewModelScope.launch {
            cartRepository.incrementQuantity(menuItemId)
        }
    }

    fun incrementQuantity(menuItemId: Long) = incrementItem(menuItemId)

    fun decrementItem(menuItemId: Long) {
        viewModelScope.launch {
            cartRepository.decrementQuantity(menuItemId)
            // Update restaurant id if cart is empty
            if (cartRepository.getCartRestaurantId() == null) {
                _cartRestaurantId.value = null
                _restaurantName.value = ""
            }
        }
    }

    fun decrementQuantity(menuItemId: Long) = decrementItem(menuItemId)

    fun removeItem(menuItemId: Long) {
        viewModelScope.launch {
            cartRepository.removeFromCartByMenuItemId(menuItemId)
            if (cartRepository.getCartRestaurantId() == null) {
                _cartRestaurantId.value = null
                _restaurantName.value = ""
            }
        }
    }

    fun removeFromCart(cartItemId: Long) {
        viewModelScope.launch {
            cartRepository.removeFromCartById(cartItemId)
            if (cartRepository.getCartRestaurantId() == null) {
                _cartRestaurantId.value = null
                _restaurantName.value = ""
            }
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            cartRepository.clearCart()
            _cartRestaurantId.value = null
            _restaurantName.value = ""
            _appliedCoupon.value = null
        }
    }

    fun getQuantityForItem(menuItemId: Long): Flow<Int> {
        return cartItems.map { items ->
            items.find { it.menuItemId == menuItemId }?.quantity ?: 0
        }
    }

    fun applyCoupon(code: String): Boolean {
        val couponInfo = when (code.uppercase()) {
            "SAVE10" -> CouponInfo("SAVE10", "10% OFF", "10% discount on your order")
            "FIRST50" -> CouponInfo("FIRST50", "₹50 OFF", "Flat ₹50 off on your order")
            "FREEDEL" -> CouponInfo("FREEDEL", "Free Delivery", "Free delivery on this order")
            else -> null
        }
        _appliedCoupon.value = couponInfo
        return couponInfo != null
    }

    fun removeCoupon() {
        _appliedCoupon.value = null
    }
}

data class CouponInfo(
    val code: String,
    val title: String,
    val description: String
)
