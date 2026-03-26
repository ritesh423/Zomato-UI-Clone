package com.riteshapps.zomatoclone.presentation.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.riteshapps.zomatoclone.presentation.components.ProfileMenuItem
import com.riteshapps.zomatoclone.presentation.navigation.Routes
import com.riteshapps.zomatoclone.presentation.navigation.SubNavigation
import com.riteshapps.zomatoclone.presentation.viewmodel.ProfileViewModel
import com.riteshapps.zomatoclone.ui.theme.DividerColor
import com.riteshapps.zomatoclone.ui.theme.ZomatoRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val userName by profileViewModel.userName.collectAsState()
    val userEmail by profileViewModel.userEmail.collectAsState()
    val isDarkMode by profileViewModel.darkMode.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        // Profile Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(ZomatoRed)
                .padding(24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = userName.take(1).uppercase(),
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = ZomatoRed
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = userName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Text(
                    text = userEmail,
                    fontSize = 13.sp,
                    color = Color.White.copy(alpha = 0.7f)
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = { navController.navigate(Routes.EditProfileScreen) },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.White
                    ),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text("Edit Profile", fontSize = 13.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Menu Items Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column {
                ProfileMenuItem(
                    icon = Icons.Default.Receipt,
                    title = "My Orders",
                    onClick = { navController.navigate(Routes.OrdersScreen) }
                )
                HorizontalDivider(color = DividerColor)
                
                ProfileMenuItem(
                    icon = Icons.Default.LocationOn,
                    title = "Saved Addresses",
                    onClick = { /* Navigate to addresses */ }
                )
                HorizontalDivider(color = DividerColor)
                
                ProfileMenuItem(
                    icon = Icons.Default.Favorite,
                    title = "Wishlist",
                    onClick = { navController.navigate(Routes.WishlistScreen) }
                )
                HorizontalDivider(color = DividerColor)
                
                ProfileMenuItem(
                    icon = Icons.AutoMirrored.Filled.Help,
                    title = "Help & Support",
                    onClick = { /* Navigate to help */ }
                )
                HorizontalDivider(color = DividerColor)
                
                ProfileMenuItem(
                    icon = Icons.Default.Info,
                    title = "About",
                    onClick = { /* Show about */ }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Settings Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column {
                ProfileMenuItem(
                    icon = Icons.Default.DarkMode,
                    title = "Dark Mode",
                    onClick = { profileViewModel.toggleDarkMode() },
                    trailing = {
                        Switch(
                            checked = isDarkMode,
                            onCheckedChange = { profileViewModel.toggleDarkMode() },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = ZomatoRed,
                                checkedTrackColor = ZomatoRed.copy(alpha = 0.5f)
                            )
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Logout
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            ProfileMenuItem(
                icon = Icons.AutoMirrored.Filled.Logout,
                title = "Logout",
                onClick = {
                    profileViewModel.logout()
                    navController.navigate(SubNavigation.AuthGraph) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                textColor = ZomatoRed,
                trailing = null
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // App version
        Text(
            text = "Version 1.0.0",
            fontSize = 12.sp,
            color = com.riteshapps.zomatoclone.ui.theme.TextSecondary,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}