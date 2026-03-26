package com.riteshapps.zomatoclone.presentation.screens

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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.riteshapps.zomatoclone.R
import com.riteshapps.zomatoclone.presentation.navigation.Routes
import com.riteshapps.zomatoclone.ui.theme.*
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiningScreen(
    navController: NavController,
    listState: LazyListState = rememberLazyListState()
) {
    val diningCategories = remember {
        listOf(
            DiningCategory(R.drawable.price_tag, "Offers", "Flat 50% OFF"),
            DiningCategory(R.drawable.collections, "Collections", "Curated lists"),
            DiningCategory(R.drawable.snack_meal, "Openings", "Fresh spots"),
            DiningCategory(R.drawable.fruits, "Healthy", "Guilt-free")
        )
    }

    val featuredRestaurants = remember {
        listOf(
            FeaturedDiningRestaurant(
                id = 1,
                name = "Punjab Grill",
                imageRes = R.drawable.restaurant1,
                cuisine = "North Indian, Mughlai",
                rating = 4.5f,
                priceForTwo = "₹1,800",
                discount = "Flat 20% OFF",
                location = "Connaught Place"
            ),
            FeaturedDiningRestaurant(
                id = 2,
                name = "Bukhara",
                imageRes = R.drawable.restaurant2,
                cuisine = "North Indian",
                rating = 4.8f,
                priceForTwo = "₹4,000",
                discount = "Complimentary dessert",
                location = "ITC Maurya"
            ),
            FeaturedDiningRestaurant(
                id = 3,
                name = "Indian Accent",
                imageRes = R.drawable.restaurant3,
                cuisine = "Modern Indian",
                rating = 4.7f,
                priceForTwo = "₹5,500",
                discount = "15% OFF on total bill",
                location = "The Lodhi"
            )
        )
    }

    val nearbyRestaurants = remember {
        listOf(
            NearbyDiningRestaurant(
                id = 4,
                name = "Farzi Cafe",
                imageRes = R.drawable.restaurant4,
                cuisine = "Modern Indian, Continental",
                rating = 4.3f,
                priceForTwo = "₹1,600",
                distance = "1.2 km",
                offer = "20% OFF with Gold"
            ),
            NearbyDiningRestaurant(
                id = 5,
                name = "Masala Library",
                imageRes = R.drawable.restaurant5,
                cuisine = "Modern Indian",
                rating = 4.6f,
                priceForTwo = "₹3,000",
                distance = "2.5 km",
                offer = "Flat 15% OFF"
            ),
            NearbyDiningRestaurant(
                id = 6,
                name = "Olive Bar & Kitchen",
                imageRes = R.drawable.restaurant6,
                cuisine = "Mediterranean, Italian",
                rating = 4.4f,
                priceForTwo = "₹2,500",
                distance = "3.8 km",
                offer = "Free dessert"
            )
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Top Bar
            item {
                DiningTopBar(
                    onProfileClick = { navController.navigate(Routes.ProfileScreen) }
                )
            }

            // Search Bar
            item {
                DiningSearchBarSection(
                    onClick = { navController.navigate(Routes.SearchBarScreen) }
                )
            }

            // Banner Image
            item {
                DiningBannerSection()
            }

            // Explore Categories
            item {
                DiningSectionTitle(title = "EXPLORE")
            }

            item {
                DiningCategoriesRow(categories = diningCategories)
            }

            // In The Limelight - Featured Restaurants
            item {
                Spacer(modifier = Modifier.height(16.dp))
                DiningSectionTitle(title = "IN THE LIMELIGHT")
            }

            item {
                FeaturedRestaurantsCarousel(
                    restaurants = featuredRestaurants,
                    onRestaurantClick = { id ->
                        navController.navigate(Routes.RestaurantDetailScreen(id))
                    }
                )
            }

            // Offers Curated For You
            item {
                Spacer(modifier = Modifier.height(16.dp))
                DiningSectionTitle(title = "OFFERS CURATED FOR YOU")
            }

            item {
                OfferCards()
            }

            // Nearby Restaurants
            item {
                Spacer(modifier = Modifier.height(16.dp))
                DiningSectionTitle(title = "RESTAURANTS NEAR YOU")
            }

            items(nearbyRestaurants) { restaurant ->
                NearbyRestaurantCard(
                    restaurant = restaurant,
                    onClick = {
                        navController.navigate(Routes.RestaurantDetailScreen(restaurant.id))
                    }
                )
            }

            // Bottom spacing
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
private fun DiningTopBar(
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

@Composable
private fun DiningSearchBarSection(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .shadow(4.dp, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search",
            tint = ZomatoRed,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = "Search for restaurants",
            fontSize = 15.sp,
            color = TextSecondary
        )
        Spacer(modifier = Modifier.weight(1f))
        HorizontalDivider(
            modifier = Modifier
                .height(24.dp)
                .width(1.dp),
            color = BorderLight
        )
        Spacer(modifier = Modifier.width(12.dp))
        Icon(
            imageVector = Icons.Default.Mic,
            contentDescription = "Voice search",
            tint = ZomatoRed,
            modifier = Modifier.size(22.dp)
        )
    }
}

@Composable
private fun DiningBannerSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .height(160.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.diningbanner),
                contentDescription = "Dining Banner",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
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
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Dine-out & Save",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Get up to 50% OFF at top restaurants",
                    fontSize = 13.sp,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
        }
    }
}

@Composable
private fun DiningSectionTitle(title: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(BorderLight)
        )
        Text(
            text = title,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = TextSecondary,
            letterSpacing = 2.sp,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(BorderLight)
        )
    }
}

@Composable
private fun DiningCategoriesRow(categories: List<DiningCategory>) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(categories) { category ->
            DiningCategoryCard(category)
        }
    }
}

@Composable
private fun DiningCategoryCard(category: DiningCategory) {
    Card(
        modifier = Modifier
            .width(85.dp)
            .clickable { },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = category.iconRes),
                contentDescription = category.name,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = category.name,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = TextPrimary,
                textAlign = TextAlign.Center
            )
            Text(
                text = category.subtitle,
                fontSize = 10.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun FeaturedRestaurantsCarousel(
    restaurants: List<FeaturedDiningRestaurant>,
    onRestaurantClick: (Long) -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { restaurants.size })

    LaunchedEffect(pagerState) {
        while (true) {
            delay(4000)
            val nextPage = (pagerState.currentPage + 1) % restaurants.size
            pagerState.animateScrollToPage(nextPage)
        }
    }

    Column {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .padding(horizontal = 16.dp),
            pageSpacing = 12.dp
        ) { page ->
            FeaturedRestaurantCard(
                restaurant = restaurants[page],
                onClick = { onRestaurantClick(restaurants[page].id) }
            )
        }

        // Page indicators
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(restaurants.size) { index ->
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .size(if (pagerState.currentPage == index) 8.dp else 6.dp)
                        .clip(CircleShape)
                        .background(
                            if (pagerState.currentPage == index) ZomatoRed
                            else Color.LightGray
                        )
                )
            }
        }
    }
}

@Composable
private fun FeaturedRestaurantCard(
    restaurant: FeaturedDiningRestaurant,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = restaurant.imageRes),
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
                                Color.Black.copy(alpha = 0.7f)
                            ),
                            startY = 100f
                        )
                    )
            )

            // Discount badge
            Box(
                modifier = Modifier
                    .padding(12.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFFFFD700))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .align(Alignment.TopStart)
            ) {
                Text(
                    text = restaurant.discount,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            // Restaurant info
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Text(
                    text = restaurant.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = restaurant.cuisine,
                    fontSize = 13.sp,
                    color = Color.White.copy(alpha = 0.9f)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(RatingGreen)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${restaurant.rating}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(10.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${restaurant.priceForTwo} for two",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "• ${restaurant.location}",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}

@Composable
private fun OfferCards() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OfferCard(
            modifier = Modifier.weight(1f),
            title = "Flat 50% OFF",
            subtitle = "On your first dine-out",
            backgroundColor = Color(0xFFFFF3E0)
        )
        OfferCard(
            modifier = Modifier.weight(1f),
            title = "Gold Benefits",
            subtitle = "Extra discounts",
            backgroundColor = Color(0xFFFCE4EC)
        )
    }
}

@Composable
private fun OfferCard(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    backgroundColor: Color
) {
    Card(
        modifier = modifier.height(90.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = TextSecondary
            )
        }
    }
}

@Composable
private fun NearbyRestaurantCard(
    restaurant: NearbyDiningRestaurant,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Restaurant image
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(10.dp))
            ) {
                Image(
                    painter = painterResource(id = restaurant.imageRes),
                    contentDescription = restaurant.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Offer badge
                if (restaurant.offer.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .clip(RoundedCornerShape(topEnd = 6.dp))
                            .background(DiscountBlue)
                            .padding(horizontal = 6.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = restaurant.offer,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Restaurant details
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = restaurant.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(RatingGreen)
                            .padding(horizontal = 5.dp, vertical = 2.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${restaurant.rating}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(10.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = restaurant.priceForTwo,
                        fontSize = 13.sp,
                        color = TextSecondary
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = restaurant.cuisine,
                    fontSize = 13.sp,
                    color = TextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = restaurant.distance,
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
            }

            // Bookmark icon
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Default.BookmarkBorder,
                    contentDescription = "Save",
                    tint = TextSecondary
                )
            }
        }
    }
}

// Data classes
data class DiningCategory(
    val iconRes: Int,
    val name: String,
    val subtitle: String
)

data class FeaturedDiningRestaurant(
    val id: Long,
    val name: String,
    val imageRes: Int,
    val cuisine: String,
    val rating: Float,
    val priceForTwo: String,
    val discount: String,
    val location: String
)

data class NearbyDiningRestaurant(
    val id: Long,
    val name: String,
    val imageRes: Int,
    val cuisine: String,
    val rating: Float,
    val priceForTwo: String,
    val distance: String,
    val offer: String
)