package com.riteshapps.zomatoclone.presentation.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.riteshapps.zomatoclone.presentation.navigation.Routes
import com.riteshapps.zomatoclone.presentation.navigation.SubNavigation
import com.riteshapps.zomatoclone.presentation.viewmodel.AuthViewModel
import com.riteshapps.zomatoclone.ui.theme.TextSecondary
import com.riteshapps.zomatoclone.ui.theme.ZomatoRed
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()
    val isAdmin by authViewModel.isAdmin.collectAsState()
    val onboardingShown by authViewModel.onboardingShown.collectAsState()

    // Animation states
    var startAnimation by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.5f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )
    val alpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 800)
    )

    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(2000)
        
        navController.popBackStack()
        
        when {
            isLoggedIn && isAdmin -> {
                navController.navigate(SubNavigation.AdminGraph) {
                    popUpTo(Routes.SplashScreen) { inclusive = true }
                }
            }
            isLoggedIn -> {
                navController.navigate(SubNavigation.MainHomeScreen) {
                    popUpTo(Routes.SplashScreen) { inclusive = true }
                }
            }
            !onboardingShown -> {
                navController.navigate(Routes.OnboardingScreen) {
                    popUpTo(Routes.SplashScreen) { inclusive = true }
                }
            }
            else -> {
                navController.navigate(Routes.LoginScreen) {
                    popUpTo(Routes.SplashScreen) { inclusive = true }
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .scale(scale)
                .alpha(alpha)
        ) {
            // Zomato Logo Text
            Text(
                text = "zomato",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = ZomatoRed,
                fontFamily = FontFamily.Serif
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Tagline
            Text(
                text = "Discover the best food & drinks",
                fontSize = 14.sp,
                color = TextSecondary
            )
        }
    }
}
