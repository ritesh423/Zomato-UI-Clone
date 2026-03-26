package com.riteshapps.zomatoclone.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riteshapps.zomatoclone.data.local.entity.RestaurantEntity
import com.riteshapps.zomatoclone.data.repository.WishlistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WishlistViewModel @Inject constructor(
    private val wishlistRepository: WishlistRepository
) : ViewModel() {

    val wishlistRestaurants: StateFlow<List<RestaurantEntity>> = wishlistRepository.getWishlistRestaurants()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val wishlistCount: StateFlow<Int> = wishlistRepository.getWishlistCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun removeFromWishlist(restaurantId: Long) {
        viewModelScope.launch {
            wishlistRepository.removeFromWishlist(restaurantId)
        }
    }

    fun clearWishlist() {
        viewModelScope.launch {
            wishlistRepository.clearWishlist()
        }
    }
}
