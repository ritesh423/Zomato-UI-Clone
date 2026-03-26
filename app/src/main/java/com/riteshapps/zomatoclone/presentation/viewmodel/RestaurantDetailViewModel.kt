package com.riteshapps.zomatoclone.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riteshapps.zomatoclone.common.UiState
import com.riteshapps.zomatoclone.data.local.entity.MenuItemEntity
import com.riteshapps.zomatoclone.data.local.entity.RestaurantEntity
import com.riteshapps.zomatoclone.data.local.entity.ReviewEntity
import com.riteshapps.zomatoclone.data.repository.MenuRepository
import com.riteshapps.zomatoclone.data.repository.RestaurantRepository
import com.riteshapps.zomatoclone.data.repository.ReviewRepository
import com.riteshapps.zomatoclone.data.repository.WishlistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RestaurantDetailViewModel @Inject constructor(
    private val restaurantRepository: RestaurantRepository,
    private val menuRepository: MenuRepository,
    private val reviewRepository: ReviewRepository,
    private val wishlistRepository: WishlistRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _restaurantId = MutableStateFlow<Long>(0)
    
    private val _restaurantState = MutableStateFlow<UiState<RestaurantEntity>>(UiState.Loading)
    val restaurantState: StateFlow<UiState<RestaurantEntity>> = _restaurantState.asStateFlow()

    val menuItems: StateFlow<List<MenuItemEntity>> = _restaurantId
        .filter { it > 0 }
        .flatMapLatest { menuRepository.getMenuItemsByRestaurant(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val menuCategories: StateFlow<List<String>> = _restaurantId
        .filter { it > 0 }
        .flatMapLatest { menuRepository.getCategoriesForRestaurant(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val reviews: StateFlow<List<ReviewEntity>> = _restaurantId
        .filter { it > 0 }
        .flatMapLatest { reviewRepository.getReviewsByRestaurant(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val isInWishlist: StateFlow<Boolean> = _restaurantId
        .filter { it > 0 }
        .flatMapLatest { wishlistRepository.isInWishlist(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    private val _selectedMenuCategory = MutableStateFlow<String?>(null)
    val selectedMenuCategory: StateFlow<String?> = _selectedMenuCategory.asStateFlow()

    private val _isVegOnly = MutableStateFlow(false)
    val isVegOnly: StateFlow<Boolean> = _isVegOnly.asStateFlow()

    val filteredMenuItems: StateFlow<List<MenuItemEntity>> = combine(
        menuItems,
        _selectedMenuCategory,
        _isVegOnly
    ) { items, category, vegOnly ->
        items.filter { item ->
            val categoryMatch = category == null || item.category == category
            val vegMatch = !vegOnly || item.isVeg
            categoryMatch && vegMatch
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun loadRestaurant(restaurantId: Long) {
        _restaurantId.value = restaurantId
        viewModelScope.launch {
            _restaurantState.value = UiState.Loading
            restaurantRepository.getRestaurantById(restaurantId).collect { restaurant ->
                _restaurantState.value = if (restaurant != null) {
                    UiState.Success(restaurant)
                } else {
                    UiState.Error("Restaurant not found")
                }
            }
        }
    }

    fun selectMenuCategory(category: String?) {
        _selectedMenuCategory.value = category
    }

    fun toggleVegOnly() {
        _isVegOnly.value = !_isVegOnly.value
    }

    fun toggleWishlist() {
        val restaurantId = _restaurantId.value
        if (restaurantId > 0) {
            viewModelScope.launch {
                wishlistRepository.toggleWishlist(restaurantId)
            }
        }
    }
}
