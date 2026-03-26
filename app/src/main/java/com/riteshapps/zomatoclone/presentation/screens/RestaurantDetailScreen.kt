package com.riteshapps.zomatoclone.presentation.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.riteshapps.zomatoclone.common.UiState
import com.riteshapps.zomatoclone.data.local.entity.MenuItemEntity
import com.riteshapps.zomatoclone.data.local.entity.RestaurantEntity
import com.riteshapps.zomatoclone.data.local.entity.ReviewEntity
import com.riteshapps.zomatoclone.presentation.components.*
import com.riteshapps.zomatoclone.presentation.navigation.Routes
import com.riteshapps.zomatoclone.presentation.viewmodel.CartViewModel
import com.riteshapps.zomatoclone.presentation.viewmodel.RestaurantDetailViewModel
import com.riteshapps.zomatoclone.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantDetailScreen(
    restaurantId: Long,
    navController: NavController,
    viewModel: RestaurantDetailViewModel = hiltViewModel(),
    cartViewModel: CartViewModel = hiltViewModel()
) {
    val restaurantState by viewModel.restaurantState.collectAsState()
    val menuItems by viewModel.menuItems.collectAsState()
    val reviews by viewModel.reviews.collectAsState()
    val isWishlisted by viewModel.isInWishlist.collectAsState()
    val cartItems by cartViewModel.cartItems.collectAsState()
    
    val cartItemCount by cartViewModel.itemCount.collectAsState()
    val cartTotal by cartViewModel.grandTotal.collectAsState()
    val cartRestaurantName by cartViewModel.restaurantName.collectAsState()

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val listState = rememberLazyListState()

    LaunchedEffect(restaurantId) {
        viewModel.loadRestaurant(restaurantId)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when (val state = restaurantState) {
            is UiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = ZomatoRed)
                }
            }
            is UiState.Success -> {
                val restaurant = state.data
                
                Scaffold(
                    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                    topBar = {
                        LargeTopAppBar(
                            title = {
                                Text(
                                    text = restaurant.name,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
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
                                IconButton(onClick = { viewModel.toggleWishlist() }) {
                                    Icon(
                                        imageVector = if (isWishlisted) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                        contentDescription = "Wishlist",
                                        tint = if (isWishlisted) ZomatoRed else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                                IconButton(onClick = { /* Share */ }) {
                                    Icon(
                                        imageVector = Icons.Default.Share,
                                        contentDescription = "Share"
                                    )
                                }
                            },
                            scrollBehavior = scrollBehavior,
                            colors = TopAppBarDefaults.largeTopAppBarColors(
                                containerColor = MaterialTheme.colorScheme.background,
                                scrolledContainerColor = MaterialTheme.colorScheme.surface
                            )
                        )
                    }
                ) { paddingValues ->
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        // Hero Image
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                            ) {
                                AsyncImage(
                                    model = restaurant.imageResId,
                                    contentDescription = restaurant.name,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                                
                                // Gradient overlay
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(
                                            Brush.verticalGradient(
                                                colors = listOf(
                                                    Color.Transparent,
                                                    Color.Black.copy(alpha = 0.6f)
                                                )
                                            )
                                        )
                                )
                            }
                        }

                        // Restaurant Info
                        item {
                            RestaurantInfoSection(restaurant = restaurant)
                        }

                        // Offers
                        if (restaurant.offerTag.isNotEmpty()) {
                            item {
                                OffersSection(offerTag = restaurant.offerTag)
                            }
                        }

                        // Menu Section Header
                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            SectionHeader(title = "Menu")
                        }

                        // Menu Items
                        if (menuItems.isEmpty()) {
                            items(3) {
                                ShimmerMenuItemRow()
                            }
                        } else {
                            val groupedMenu = menuItems.groupBy { it.category }
                            
                            groupedMenu.forEach { (category, items) ->
                                item {
                                    MenuCategoryHeader(
                                        category = category,
                                        itemCount = items.size
                                    )
                                }
                                
                                items(
                                    items = items,
                                    key = { it.id }
                                ) { menuItem ->
                                    val quantity = cartItems
                                        .find { it.menuItemId == menuItem.id }
                                        ?.quantity ?: 0
                                    
                                    MenuItemRow(
                                        menuItem = menuItem,
                                        quantity = quantity,
                                        onAddClick = {
                                            cartViewModel.addItem(
                                                menuItem = menuItem,
                                                restaurantId = restaurant.id,
                                                restaurantName = restaurant.name
                                            )
                                        },
                                        onIncrement = {
                                            cartViewModel.incrementItem(menuItem.id)
                                        },
                                        onDecrement = {
                                            cartViewModel.decrementItem(menuItem.id)
                                        }
                                    )
                                    HorizontalDivider(color = DividerColor)
                                }
                            }
                        }

                        // Reviews Section
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                            SectionHeader(
                                title = "Reviews",
                                showSeeAll = true,
                                onSeeAllClick = { /* Show all reviews */ }
                            )
                        }

                        if (reviews.isNotEmpty()) {
                            items(
                                items = reviews.take(3),
                                key = { it.id }
                            ) { review ->
                                ReviewCard(review = review)
                            }
                        }

                        // Bottom spacing for cart bar
                        item {
                            Spacer(modifier = Modifier.height(80.dp))
                        }
                    }
                }
            }
            is UiState.Error -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = state.message, color = MaterialTheme.colorScheme.error)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { viewModel.loadRestaurant(restaurantId) }) {
                        Text("Retry")
                    }
                }
            }
            UiState.Idle -> {}
        }

        // Floating Cart Bar
        FloatingCartBar(
            itemCount = cartItemCount,
            totalAmount = cartTotal,
            restaurantName = cartRestaurantName,
            onViewCartClick = { navController.navigate(Routes.CartScreen) },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun RestaurantInfoSection(restaurant: RestaurantEntity) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Cuisine & Category
        Text(
            text = restaurant.cuisine,
            fontSize = 14.sp,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Info row
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            RatingBadge(rating = restaurant.rating)
            
            Text(
                text = "1000+ ratings",
                fontSize = 13.sp,
                color = TextSecondary
            )
            
            Text(text = "•", color = TextSecondary)
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Schedule,
                    contentDescription = null,
                    tint = TextSecondary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = restaurant.deliveryTime,
                    fontSize = 13.sp,
                    color = TextSecondary
                )
            }
            
            Text(text = "•", color = TextSecondary)
            
            Text(
                text = restaurant.distance,
                fontSize = 13.sp,
                color = TextSecondary
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Status
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(if (restaurant.isOpen) RatingGreen else MaterialTheme.colorScheme.error)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = if (restaurant.isOpen) "Open now" else "Closed",
                fontSize = 13.sp,
                color = if (restaurant.isOpen) RatingGreen else MaterialTheme.colorScheme.error,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun OffersSection(offerTag: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OfferTag(text = offerTag)
        OfferTag(text = "Free delivery above ₹499")
    }
}

@Composable
private fun ReviewCard(review: ReviewEntity) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(ZomatoRedLight),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = review.userName.take(1).uppercase(),
                        color = ZomatoRed,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = review.userName,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = java.text.SimpleDateFormat("MMM dd, yyyy", java.util.Locale.getDefault())
                            .format(java.util.Date(review.timestamp)),
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }

                RatingBadge(rating = review.rating)
            }

            if (review.comment.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = review.comment,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
