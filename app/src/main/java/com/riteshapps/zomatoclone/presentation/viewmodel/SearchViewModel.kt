package com.riteshapps.zomatoclone.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riteshapps.zomatoclone.data.local.datastore.SearchDataStore
import com.riteshapps.zomatoclone.data.local.entity.CategoryEntity
import com.riteshapps.zomatoclone.data.local.entity.MenuItemEntity
import com.riteshapps.zomatoclone.data.local.entity.RestaurantEntity
import com.riteshapps.zomatoclone.data.repository.CategoryRepository
import com.riteshapps.zomatoclone.data.repository.MenuRepository
import com.riteshapps.zomatoclone.data.repository.RestaurantRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val restaurantRepository: RestaurantRepository,
    private val menuRepository: MenuRepository,
    private val categoryRepository: CategoryRepository,
    private val searchDataStore: SearchDataStore
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val recentSearches: StateFlow<Set<String>> = searchDataStore.recentSearches
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    val categories: StateFlow<List<CategoryEntity>> = categoryRepository.getAllCategories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(FlowPreview::class)
    val searchResults: StateFlow<SearchResults> = _searchQuery
        .debounce(300)
        .flatMapLatest { query ->
            if (query.length < 2) {
                flowOf(SearchResults())
            } else {
                combine(
                    restaurantRepository.searchRestaurants(query),
                    menuRepository.searchMenuItems(query)
                ) { restaurants, menuItems ->
                    SearchResults(restaurants, menuItems)
                }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SearchResults())

    private val _isSearchActive = MutableStateFlow(false)
    val isSearchActive: StateFlow<Boolean> = _isSearchActive.asStateFlow()

    // Filters
    private val _isVegOnly = MutableStateFlow(false)
    val isVegOnly: StateFlow<Boolean> = _isVegOnly.asStateFlow()

    private val _minRating = MutableStateFlow(0f)
    val minRating: StateFlow<Float> = _minRating.asStateFlow()

    private val _maxDeliveryTime = MutableStateFlow<Int?>(null)
    val maxDeliveryTime: StateFlow<Int?> = _maxDeliveryTime.asStateFlow()

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSearchActive(active: Boolean) {
        _isSearchActive.value = active
    }

    fun addToRecentSearches(query: String) {
        viewModelScope.launch {
            searchDataStore.addRecentSearch(query)
        }
    }

    fun removeRecentSearch(query: String) {
        viewModelScope.launch {
            searchDataStore.removeRecentSearch(query)
        }
    }

    fun clearRecentSearches() {
        viewModelScope.launch {
            searchDataStore.clearRecentSearches()
        }
    }

    fun toggleVegOnly() {
        _isVegOnly.value = !_isVegOnly.value
    }

    fun setMinRating(rating: Float) {
        _minRating.value = rating
    }

    fun setMaxDeliveryTime(time: Int?) {
        _maxDeliveryTime.value = time
    }

    fun clearFilters() {
        _isVegOnly.value = false
        _minRating.value = 0f
        _maxDeliveryTime.value = null
    }
}

data class SearchResults(
    val restaurants: List<RestaurantEntity> = emptyList(),
    val menuItems: List<MenuItemEntity> = emptyList()
)
