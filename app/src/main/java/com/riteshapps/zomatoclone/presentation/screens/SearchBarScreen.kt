package com.riteshapps.zomatoclone.presentation.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.riteshapps.zomatoclone.data.local.entity.CategoryEntity
import com.riteshapps.zomatoclone.data.local.entity.MenuItemEntity
import com.riteshapps.zomatoclone.data.local.entity.RestaurantEntity
import com.riteshapps.zomatoclone.presentation.components.*
import com.riteshapps.zomatoclone.presentation.navigation.Routes
import com.riteshapps.zomatoclone.presentation.viewmodel.SearchViewModel
import com.riteshapps.zomatoclone.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarScreen(
    navController: NavController,
    searchViewModel: SearchViewModel = hiltViewModel()
) {
    val searchQuery by searchViewModel.searchQuery.collectAsState()
    val recentSearches by searchViewModel.recentSearches.collectAsState()
    val searchResults by searchViewModel.searchResults.collectAsState()
    val categories by searchViewModel.categories.collectAsState()
    val isSearching = searchQuery.length >= 2
    
    var showFilterSheet by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchViewModel.updateSearchQuery(it) },
                        placeholder = { 
                            Text(
                                "Search for restaurants, cuisines",
                                fontSize = 14.sp,
                                color = TextSecondary
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            cursorColor = ZomatoRed
                        ),
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchViewModel.updateSearchQuery("") }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Clear"
                                    )
                                }
                            }
                        }
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showFilterSheet = true }) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Filter"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        AnimatedContent(
            targetState = isSearching,
            transitionSpec = {
                fadeIn() togetherWith fadeOut()
            },
            modifier = Modifier.padding(paddingValues)
        ) { searching ->
            if (!searching) {
                // Default content: Recent searches + Popular cuisines
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Recent searches
                    if (recentSearches.isNotEmpty()) {
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Recent Searches",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                TextButton(onClick = { searchViewModel.clearRecentSearches() }) {
                                    Text("Clear All", color = ZomatoRed, fontSize = 13.sp)
                                }
                            }
                        }

                        item {
                            LazyRow(
                                contentPadding = PaddingValues(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(recentSearches.toList()) { search ->
                                    InputChip(
                                        selected = false,
                                        onClick = { searchViewModel.updateSearchQuery(search) },
                                        label = { Text(search) },
                                        trailingIcon = {
                                            IconButton(
                                                onClick = { searchViewModel.removeRecentSearch(search) },
                                                modifier = Modifier.size(18.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Close,
                                                    contentDescription = "Remove",
                                                    modifier = Modifier.size(14.dp)
                                                )
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // Popular cuisines
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        SectionHeader(title = "Popular Cuisines")
                    }

                    item {
                        if (categories.isNotEmpty()) {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(4),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(240.dp)
                                    .padding(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                userScrollEnabled = false
                            ) {
                                items(
                                    items = categories.take(8),
                                    key = { it.id }
                                ) { category ->
                                    CuisineGridItem(
                                        category = category,
                                        onClick = { searchViewModel.updateSearchQuery(category.name) }
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                // Search results
                val restaurants = searchResults.restaurants
                val dishes = searchResults.menuItems
                
                if (restaurants.isEmpty() && dishes.isEmpty()) {
                    // No results
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.SearchOff,
                                contentDescription = null,
                                tint = TextSecondary,
                                modifier = Modifier.size(64.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "No results found",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "Try searching for something else",
                                fontSize = 14.sp,
                                color = TextSecondary
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // Restaurants section
                        if (restaurants.isNotEmpty()) {
                            item {
                                SectionHeader(title = "Restaurants")
                            }
                            
                            items(
                                items = restaurants,
                                key = { "restaurant_${it.id}" }
                            ) { restaurant ->
                                SearchRestaurantItem(
                                    restaurant = restaurant,
                                    onClick = {
                                        searchViewModel.addToRecentSearches(searchQuery)
                                        navController.navigate(
                                            Routes.RestaurantDetailScreen(restaurant.id)
                                        )
                                    }
                                )
                            }
                        }

                        // Dishes section
                        if (dishes.isNotEmpty()) {
                            item {
                                Spacer(modifier = Modifier.height(8.dp))
                                SectionHeader(title = "Dishes")
                            }
                            
                            items(
                                items = dishes,
                                key = { "dish_${it.id}" }
                            ) { dish ->
                                SearchDishItem(
                                    dish = dish,
                                    onClick = {
                                        searchViewModel.addToRecentSearches(searchQuery)
                                        navController.navigate(
                                            Routes.RestaurantDetailScreen(dish.restaurantId)
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Filter bottom sheet
    if (showFilterSheet) {
        FilterBottomSheet(
            onDismiss = { showFilterSheet = false },
            searchViewModel = searchViewModel
        )
    }
}

@Composable
private fun CuisineGridItem(
    category: CategoryEntity,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(BackgroundGrey),
            contentAlignment = Alignment.Center
        ) {
            // Placeholder icon
            Icon(
                imageVector = Icons.Default.Restaurant,
                contentDescription = null,
                tint = TextSecondary,
                modifier = Modifier.size(28.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = category.name,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
private fun SearchRestaurantItem(
    restaurant: RestaurantEntity,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(BackgroundGrey),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Restaurant,
                contentDescription = null,
                tint = TextSecondary
            )
        }
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = restaurant.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = restaurant.cuisine,
                fontSize = 12.sp,
                color = TextSecondary
            )
        }
        
        RatingBadge(rating = restaurant.rating)
    }
}

@Composable
private fun SearchDishItem(
    dish: MenuItemEntity,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        VegIndicator(isVeg = dish.isVeg)
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = dish.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "₹${dish.price}",
                fontSize = 12.sp,
                color = TextSecondary
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterBottomSheet(
    onDismiss: () -> Unit,
    searchViewModel: SearchViewModel
) {
    val vegOnly by searchViewModel.isVegOnly.collectAsState()
    val ratingFilter by searchViewModel.minRating.collectAsState()
    val deliveryTimeFilter by searchViewModel.maxDeliveryTime.collectAsState()

    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Text(
                text = "Filters",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Veg only
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    VegIndicator(isVeg = true)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Veg Only", fontSize = 14.sp)
                }
                Switch(
                    checked = vegOnly,
                    onCheckedChange = { searchViewModel.toggleVegOnly() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = RatingGreen,
                        checkedTrackColor = RatingGreen.copy(alpha = 0.5f)
                    )
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Rating filter
            Text(
                text = "Rating",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(0f, 3.5f, 4.0f, 4.5f).forEach { rating ->
                    FilterChip(
                        selected = ratingFilter == rating,
                        onClick = { searchViewModel.setMinRating(rating) },
                        label = { 
                            Text(
                                if (rating == 0f) "Any" else "${rating}+"
                            )
                        },
                        leadingIcon = if (rating > 0f) {
                            {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        } else null,
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = ZomatoRedLight,
                            selectedLabelColor = ZomatoRed
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Delivery time filter
            Text(
                text = "Delivery Time",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Any" to null, "< 30 min" to 30, "< 45 min" to 45).forEach { (label, time) ->
                    FilterChip(
                        selected = deliveryTimeFilter == time,
                        onClick = { searchViewModel.setMaxDeliveryTime(time) },
                        label = { Text(label) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = ZomatoRedLight,
                            selectedLabelColor = ZomatoRed
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { searchViewModel.clearFilters() },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Clear All")
                }
                
                ZomatoPrimaryButton(
                    text = "Apply",
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}