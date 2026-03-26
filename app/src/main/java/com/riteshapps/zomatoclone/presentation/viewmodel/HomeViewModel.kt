package com.riteshapps.zomatoclone.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.riteshapps.zomatoclone.data.local.entity.BannerEntity
import com.riteshapps.zomatoclone.data.local.entity.CategoryEntity
import com.riteshapps.zomatoclone.data.local.entity.RestaurantEntity
import com.riteshapps.zomatoclone.data.repository.BannerRepository
import com.riteshapps.zomatoclone.data.repository.CategoryRepository
import com.riteshapps.zomatoclone.data.repository.RestaurantRepository
import com.riteshapps.zomatoclone.data.repository.WishlistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val restaurantRepository: RestaurantRepository,
    private val categoryRepository: CategoryRepository,
    private val bannerRepository: BannerRepository,
    private val wishlistRepository: WishlistRepository
) : ViewModel() {

    val categories: StateFlow<List<CategoryEntity>> = categoryRepository.getAllCategories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val banners: StateFlow<List<BannerEntity>> = bannerRepository.getAllBanners()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val restaurants: StateFlow<List<RestaurantEntity>> = restaurantRepository.getAllRestaurants()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val topRestaurants: StateFlow<List<RestaurantEntity>> = restaurantRepository.getTopRatedRestaurants(6)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val pagedRestaurants: Flow<PagingData<RestaurantEntity>> = restaurantRepository.getRestaurantsPaged()
        .cachedIn(viewModelScope)

    private val _selectedCategoryId = MutableStateFlow<Long?>(null)
    val selectedCategoryId: StateFlow<Long?> = _selectedCategoryId.asStateFlow()

    val filteredRestaurants: StateFlow<List<RestaurantEntity>> = _selectedCategoryId
        .flatMapLatest { categoryId ->
            if (categoryId == null) {
                restaurantRepository.getAllRestaurants()
            } else {
                restaurantRepository.getRestaurantsByCategory(categoryId)
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _isVegModeEnabled = MutableStateFlow(false)
    val isVegModeEnabled: StateFlow<Boolean> = _isVegModeEnabled.asStateFlow()

    fun selectCategory(categoryId: Long?) {
        _selectedCategoryId.value = categoryId
    }

    fun toggleVegMode() {
        _isVegModeEnabled.value = !_isVegModeEnabled.value
    }

    fun isInWishlist(restaurantId: Long): Flow<Boolean> {
        return wishlistRepository.isInWishlist(restaurantId)
    }

    fun toggleWishlist(restaurantId: Long) {
        viewModelScope.launch {
            wishlistRepository.toggleWishlist(restaurantId)
        }
    }
}
