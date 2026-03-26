package com.riteshapps.zomatoclone.presentation.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.riteshapps.zomatoclone.R
import com.riteshapps.zomatoclone.presentation.screens.*
import com.riteshapps.zomatoclone.presentation.viewmodel.CartViewModel
import com.riteshapps.zomatoclone.ui.theme.ZomatoRed

data class BottomNavItem(
    val title: String,
    val icon: Painter
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(
    isVisible: Boolean = true,
    listState: LazyListState = rememberLazyListState()
) {
    val navController = rememberNavController()
    val cartViewModel: CartViewModel = hiltViewModel()
    val cartItemCount by cartViewModel.cartItemCount.collectAsState()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route

    var shouldShowBottomBar by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(currentDestination) {
        shouldShowBottomBar = when (currentDestination) {
            Routes.DeliveryScreen::class.qualifiedName,
            Routes.DiningScreen::class.qualifiedName -> true
            else -> false
        }
    }

    var selectedItemIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    val BottomNavItems = listOf(
        BottomNavItem(
            title = "Delivery",
            icon = painterResource(R.drawable.delivery_cart)
        ),
        BottomNavItem(
            title = "Dining",
            icon = painterResource(R.drawable.dining)
        )
    )

    val selectedColor = ZomatoRed

    val bottomBarHeight by animateDpAsState(
        targetValue = if (isVisible) 70.dp else 0.dp,
        label = "bottomBarHeight"
    )

    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                modifier = Modifier
                    .fillMaxWidth(),
                visible = shouldShowBottomBar,
                enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
                exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth(),
                    shadowElevation = 12.dp,
                    color = Color.White
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                WindowInsets.navigationBars
                                    .only(WindowInsetsSides.Bottom)
                                    .asPaddingValues()
                            )
                    ) {
                        // Top indicator bar
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(3.dp)
                        ) {
                            BottomNavItems.forEachIndexed { index, _ ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(3.dp)
                                        .padding(horizontal = 24.dp)
                                        .clip(RoundedCornerShape(bottomStart = 3.dp, bottomEnd = 3.dp))
                                        .background(
                                            if (index == selectedItemIndex) selectedColor
                                            else Color.Transparent
                                        )
                                )
                            }
                        }

                        // Navigation items
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(64.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            BottomNavItems.forEachIndexed { index, item ->
                                val isSelected = selectedItemIndex == index
                                
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight()
                                        .clickable(
                                            interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                                            indication = null
                                        ) {
                                            selectedItemIndex = index
                                            when (index) {
                                                0 -> navController.navigate(Routes.DeliveryScreen) {
                                                    popUpTo(Routes.DeliveryScreen) { inclusive = true }
                                                }
                                                1 -> navController.navigate(Routes.DiningScreen) {
                                                    popUpTo(Routes.DeliveryScreen)
                                                }
                                            }
                                        },
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        painter = item.icon,
                                        contentDescription = item.title,
                                        modifier = Modifier.size(26.dp),
                                        tint = if (isSelected) selectedColor else Color.Gray
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = item.title,
                                        fontSize = 12.sp,
                                        fontWeight = if (isSelected) androidx.compose.ui.text.font.FontWeight.Bold else androidx.compose.ui.text.font.FontWeight.Normal,
                                        color = if (isSelected) selectedColor else Color.Gray
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            NavHost(
                navController = navController,
                startDestination = Routes.SplashScreen
            ) {
                // Splash Screen
                composable<Routes.SplashScreen> {
                    SplashScreen(navController = navController)
                }

                // Auth Graph
                navigation<SubNavigation.AuthGraph>(
                    startDestination = Routes.LoginScreen
                ) {
                    composable<Routes.OnboardingScreen> {
                        OnboardingScreen(navController = navController)
                    }

                    composable<Routes.LoginScreen> {
                        LoginScreen(navController = navController)
                    }

                    composable<Routes.SignUpScreen> {
                        SignUpScreen(navController = navController)
                    }

                    composable<Routes.ForgotPasswordScreen> {
                        ForgotPasswordScreen(navController = navController)
                    }
                }

                // Main Home Graph
                navigation<SubNavigation.MainHomeScreen>(
                    startDestination = Routes.DeliveryScreen
                ) {
                    composable<Routes.DeliveryScreen> {
                        DeliveryScreen(
                            navController = navController,
                            listState = listState,
                            cartViewModel = cartViewModel
                        )
                    }

                    composable<Routes.QuickScreen> {
                        QuickScreen(navController = navController, listState = listState)
                    }

                    composable<Routes.DiningScreen> {
                        DiningScreen(navController = navController, listState = listState)
                    }

                    composable<Routes.ProfileScreen> {
                        ProfileScreen(navController = navController)
                    }

                    composable<Routes.EditProfileScreen> {
                        EditProfileScreen(navController = navController)
                    }

                    composable<Routes.RestaurantDetailScreen> { backStackEntry ->
                        val args = backStackEntry.toRoute<Routes.RestaurantDetailScreen>()
                        RestaurantDetailScreen(
                            restaurantId = args.restaurantId,
                            navController = navController,
                            cartViewModel = cartViewModel
                        )
                    }

                    composable<Routes.CartScreen> {
                        CartScreen(
                            navController = navController,
                            cartViewModel = cartViewModel
                        )
                    }

                    composable<Routes.CheckoutScreen> {
                        CheckoutScreen(
                            navController = navController,
                            cartViewModel = cartViewModel
                        )
                    }

                    composable<Routes.OrderSuccessScreen> {
                        OrderSuccessScreen(navController = navController)
                    }

                    composable<Routes.OrdersScreen> {
                        OrdersScreen(navController = navController)
                    }

                    composable<Routes.WishlistScreen> {
                        WishlistScreen(navController = navController)
                    }

                    composable<Routes.SearchBarScreen> {
                        SearchBarScreen(navController = navController)
                    }
                }

                // Admin Graph
                navigation<SubNavigation.AdminGraph>(
                    startDestination = Routes.AdminDashboard
                ) {
                    composable<Routes.AdminDashboard> {
                        AdminDashboardScreen(navController = navController)
                    }
                }
            }
        }
    }
}
