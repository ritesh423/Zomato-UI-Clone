package com.riteshapps.zomatoclone.presentation.screens

import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.riteshapps.zomatoclone.data.local.entity.BannerEntity
import com.riteshapps.zomatoclone.data.local.entity.CategoryEntity
import com.riteshapps.zomatoclone.data.local.entity.RestaurantEntity
import com.riteshapps.zomatoclone.presentation.components.*
import com.riteshapps.zomatoclone.presentation.navigation.Routes
import com.riteshapps.zomatoclone.presentation.viewmodel.CartViewModel
import com.riteshapps.zomatoclone.presentation.viewmodel.HomeViewModel
import com.riteshapps.zomatoclone.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DeliveryScreen(
    navController: NavController,
    listState: LazyListState = rememberLazyListState(),
    homeViewModel: HomeViewModel = hiltViewModel(),
    cartViewModel: CartViewModel = hiltViewModel()
) {
    val restaurants by homeViewModel.restaurants.collectAsState()
    val categories by homeViewModel.categories.collectAsState()
    val banners by homeViewModel.banners.collectAsState()
    val selectedCategoryId by homeViewModel.selectedCategoryId.collectAsState()
    val filteredRestaurants by homeViewModel.filteredRestaurants.collectAsState()
    
    val cartItemCount by cartViewModel.itemCount.collectAsState()
    val cartTotal by cartViewModel.grandTotal.collectAsState()
    val cartRestaurantName by cartViewModel.restaurantName.collectAsState()

    val coroutineScope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Top bar - Location & Profile
            item {
                TopLocationBar(
                    onProfileClick = { navController.navigate(Routes.ProfileScreen) }
                )
            }

            // Search bar
            item {
                ZomatoSearchBar(
                    query = "",
                    onQueryChange = {},
                    onSearchClick = { navController.navigate(Routes.SearchBarScreen) },
                    enabled = false,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            // Banners
            item {
                if (banners.isEmpty()) {
                    ShimmerBannerCard(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                } else {
                    BannerSection(
                        banners = banners,
                        onBannerClick = { /* Navigate to offer */ }
                    )
                }
            }

            // Categories
            item {
                SectionHeader(title = "What's on your mind?")
            }

            item {
                if (categories.isEmpty()) {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(6) {
                            ShimmerCategoryChip()
                        }
                    }
                } else {
                    CategoriesRow(
                        categories = categories,
                        selectedCategoryId = selectedCategoryId,
                        onCategoryClick = { homeViewModel.selectCategory(it) }
                    )
                }
            }

            // Restaurants Section
            item {
                Spacer(modifier = Modifier.height(16.dp))
                SectionHeader(
                    title = "All restaurants",
                    showSeeAll = true,
                    onSeeAllClick = { /* Show all restaurants */ }
                )
            }

            val displayRestaurants = if (selectedCategoryId != null) filteredRestaurants else restaurants
            
            if (displayRestaurants.isEmpty()) {
                items(3) {
                    ShimmerRestaurantCard(modifier = Modifier.padding(horizontal = 16.dp))
                }
            } else {
                items(
                    items = displayRestaurants,
                    key = { it.id }
                ) { restaurant ->
                    var isWishlisted by remember { mutableStateOf(false) }
                    
                    LaunchedEffect(restaurant.id) {
                        homeViewModel.isInWishlist(restaurant.id).collect { isWishlisted = it }
                    }
                    
                    RestaurantCard(
                        restaurant = restaurant,
                        isWishlisted = isWishlisted,
                        onWishlistToggle = { homeViewModel.toggleWishlist(restaurant.id) },
                        onClick = { 
                            navController.navigate(Routes.RestaurantDetailScreen(restaurant.id)) 
                        },
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }

            // Bottom padding for cart bar
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }

        // Floating Cart Bar
        if (cartItemCount > 0) {
            FloatingCartBar(
                itemCount = cartItemCount,
                totalAmount = cartTotal,
                restaurantName = cartRestaurantName,
                onViewCartClick = { navController.navigate(Routes.CartScreen) },
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
private fun TopLocationBar(
    onProfileClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { /* Location picker */ }
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = ZomatoRed,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Home",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Text(
                    text = "123 Main Street, City",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }
        }

        Row {
            IconButton(onClick = { /* Notifications */ }) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = "Notifications",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(ZomatoRedLight)
                    .clickable(onClick = onProfileClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile",
                    tint = ZomatoRed,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun BannerSection(
    banners: List<BannerEntity>,
    onBannerClick: (BannerEntity) -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { banners.size })
    val coroutineScope = rememberCoroutineScope()

    // Auto-scroll
    LaunchedEffect(pagerState) {
        while (true) {
            delay(3000)
            val nextPage = (pagerState.currentPage + 1) % banners.size
            pagerState.animateScrollToPage(
                page = nextPage,
                animationSpec = tween(400)
            )
        }
    }

    Column {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .padding(horizontal = 16.dp)
        ) { page ->
            OfferBannerCard(
                banner = banners[page],
                onClick = { onBannerClick(banners[page]) }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        BannerPagerIndicator(
            pageCount = banners.size,
            currentPage = pagerState.currentPage,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun CategoriesRow(
    categories: List<CategoryEntity>,
    selectedCategoryId: Long?,
    onCategoryClick: (Long?) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = categories,
            key = { it.id }
        ) { category ->
            CategoryChip(
                category = category,
                isSelected = selectedCategoryId == category.id,
                onClick = {
                    onCategoryClick(
                        if (selectedCategoryId == category.id) null else category.id
                    )
                }
            )
        }
    }
}

@Composable
private fun ErrorState(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = null,
            tint = ZomatoRed,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = message,
            color = TextSecondary,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedButton(onClick = onRetry) {
            Text("Retry", color = ZomatoRed)
        }
    }
}