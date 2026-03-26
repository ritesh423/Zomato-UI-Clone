package com.riteshapps.zomatoclone.presentation.navigation

import kotlinx.serialization.Serializable

sealed class SubNavigation {
    @Serializable
    object AuthGraph : SubNavigation()

    @Serializable
    object MainHomeScreen : SubNavigation()

    @Serializable
    object AdminGraph : SubNavigation()
}

sealed class Routes {
    // Auth Routes
    @Serializable
    object SplashScreen : Routes()

    @Serializable
    object OnboardingScreen : Routes()

    @Serializable
    object LoginScreen : Routes()

    @Serializable
    object SignUpScreen : Routes()

    @Serializable
    object ForgotPasswordScreen : Routes()

    // Main App Routes
    @Serializable
    object DeliveryScreen : Routes()

    @Serializable
    object QuickScreen : Routes()

    @Serializable
    object DiningScreen : Routes()

    @Serializable
    object ProfileScreen : Routes()

    @Serializable
    object EditProfileScreen : Routes()

    @Serializable
    data class RestaurantDetailScreen(val restaurantId: Long) : Routes()

    @Serializable
    object CartScreen : Routes()

    @Serializable
    object CheckoutScreen : Routes()

    @Serializable
    object OrderSuccessScreen : Routes()

    @Serializable
    object OrdersScreen : Routes()

    @Serializable
    object WishlistScreen : Routes()

    @Serializable
    object SearchBarScreen : Routes()

    @Serializable
    object FinalCheckoutScreen : Routes()

    @Serializable
    object ParticularCardScreen : Routes()

    // Admin Routes
    @Serializable
    object AdminDashboard : Routes()

    @Serializable
    object AdminRestaurants : Routes()

    @Serializable
    object AdminMenu : Routes()

    @Serializable
    object AdminBanners : Routes()
}