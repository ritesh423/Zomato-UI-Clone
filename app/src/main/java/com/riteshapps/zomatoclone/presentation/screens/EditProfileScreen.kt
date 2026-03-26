package com.riteshapps.zomatoclone.presentation.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.riteshapps.zomatoclone.presentation.components.ZomatoPrimaryButton
import com.riteshapps.zomatoclone.presentation.components.ZomatoTextField
import com.riteshapps.zomatoclone.presentation.viewmodel.ProfileViewModel
import com.riteshapps.zomatoclone.ui.theme.TextSecondary
import com.riteshapps.zomatoclone.ui.theme.ZomatoRed
import com.riteshapps.zomatoclone.ui.theme.ZomatoRedLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    navController: NavController,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val userName by profileViewModel.userName.collectAsState()
    val userEmail by profileViewModel.userEmail.collectAsState()
    val userPhone by profileViewModel.userPhone.collectAsState()

    var name by remember { mutableStateOf(userName) }
    var email by remember { mutableStateOf(userEmail) }
    var phone by remember { mutableStateOf(userPhone) }
    var showAvatarSheet by remember { mutableStateOf(false) }
    var selectedAvatar by remember { mutableIntStateOf(0) }

    LaunchedEffect(userName, userEmail, userPhone) {
        name = userName
        email = userEmail
        phone = userPhone
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Profile") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Avatar
            Box(
                modifier = Modifier.clickable { showAvatarSheet = true }
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(ZomatoRedLight),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = name.take(1).uppercase(),
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = ZomatoRed
                    )
                }

                // Edit overlay
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(ZomatoRed)
                        .align(Alignment.BottomEnd),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit avatar",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Form fields
            ZomatoTextField(
                value = name,
                onValueChange = { name = it },
                label = "Full Name",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = TextSecondary
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            ZomatoTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null,
                        tint = TextSecondary
                    )
                },
                enabled = false // Email cannot be changed
            )

            Spacer(modifier = Modifier.height(16.dp))

            ZomatoTextField(
                value = phone,
                onValueChange = { phone = it },
                label = "Phone Number",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = null,
                        tint = TextSecondary
                    )
                }
            )

            Spacer(modifier = Modifier.weight(1f))

            ZomatoPrimaryButton(
                text = "Save Changes",
                onClick = {
                    profileViewModel.updateProfile(name, phone)
                    navController.popBackStack()
                }
            )
        }
    }

    // Avatar selection bottom sheet
    if (showAvatarSheet) {
        ModalBottomSheet(
            onDismissRequest = { showAvatarSheet = false }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Text(
                    text = "Choose Avatar",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Avatar options grid
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    val colors = listOf(
                        ZomatoRedLight,
                        Color(0xFFE3F2FD),
                        Color(0xFFF3E5F5),
                        Color(0xFFE8F5E9)
                    )

                    colors.forEachIndexed { index, color ->
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(color)
                                .border(
                                    width = if (selectedAvatar == index) 3.dp else 0.dp,
                                    color = if (selectedAvatar == index) ZomatoRed else Color.Transparent,
                                    shape = CircleShape
                                )
                                .clickable {
                                    selectedAvatar = index
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = name.take(1).uppercase(),
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = when (index) {
                                    0 -> ZomatoRed
                                    1 -> Color(0xFF1976D2)
                                    2 -> Color(0xFF7B1FA2)
                                    else -> Color(0xFF388E3C)
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                ZomatoPrimaryButton(
                    text = "Select",
                    onClick = { showAvatarSheet = false }
                )

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}
