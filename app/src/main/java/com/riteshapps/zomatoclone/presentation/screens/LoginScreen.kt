package com.riteshapps.zomatoclone.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.riteshapps.zomatoclone.common.UiState
import com.riteshapps.zomatoclone.presentation.components.ZomatoPrimaryButton
import com.riteshapps.zomatoclone.presentation.components.ZomatoTextField
import com.riteshapps.zomatoclone.presentation.navigation.Routes
import com.riteshapps.zomatoclone.presentation.navigation.SubNavigation
import com.riteshapps.zomatoclone.presentation.viewmodel.AuthViewModel
import com.riteshapps.zomatoclone.ui.theme.BorderLight
import com.riteshapps.zomatoclone.ui.theme.TextSecondary
import com.riteshapps.zomatoclone.ui.theme.ZomatoRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val loginState by authViewModel.loginState.collectAsState()
    val isAdmin by authViewModel.isAdmin.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    // Handle login success
    LaunchedEffect(loginState) {
        if (loginState is UiState.Success) {
            if (isAdmin) {
                navController.navigate(SubNavigation.AdminGraph) {
                    popUpTo(SubNavigation.AuthGraph) { inclusive = true }
                }
            } else {
                navController.navigate(SubNavigation.MainHomeScreen) {
                    popUpTo(SubNavigation.AuthGraph) { inclusive = true }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        // Back button placeholder (for visual consistency)
        Spacer(modifier = Modifier.height(16.dp))

        // Title
        Text(
            text = "Log in",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row {
            Text(
                text = "or ",
                fontSize = 14.sp,
                color = TextSecondary
            )
            Text(
                text = "create an account",
                fontSize = 14.sp,
                color = ZomatoRed,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable {
                    navController.navigate(Routes.SignUpScreen)
                }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Email field
        ZomatoTextField(
            value = email,
            onValueChange = {
                email = it
                emailError = null
            },
            label = "Email",
            isError = emailError != null,
            errorMessage = emailError,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = null,
                    tint = TextSecondary
                )
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password field
        ZomatoTextField(
            value = password,
            onValueChange = {
                password = it
                passwordError = null
            },
            label = "Password",
            isError = passwordError != null,
            errorMessage = passwordError,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = TextSecondary
                )
            },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = if (passwordVisible) "Hide password" else "Show password",
                        tint = TextSecondary
                    )
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation()
        )

        // Forgot password
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(
                onClick = { navController.navigate(Routes.ForgotPasswordScreen) }
            ) {
                Text(
                    text = "Forgot Password?",
                    color = ZomatoRed,
                    fontSize = 13.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Error message
        if (loginState is UiState.Error) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = (loginState as UiState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(12.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Login button
        ZomatoPrimaryButton(
            text = "Continue",
            onClick = {
                // Validate
                var isValid = true
                if (email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailError = "Please enter a valid email"
                    isValid = false
                }
                if (password.length < 6) {
                    passwordError = "Password must be at least 6 characters"
                    isValid = false
                }
                if (isValid) {
                    authViewModel.login(email, password)
                }
            },
            isLoading = loginState is UiState.Loading
        )

        Spacer(modifier = Modifier.height(24.dp))

        // OR divider
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                color = BorderLight
            )
            Text(
                text = "  OR  ",
                color = TextSecondary,
                fontSize = 13.sp
            )
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                color = BorderLight
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Google login button (placeholder)
        OutlinedButton(
            onClick = { /* Mock Google login - just use regular login */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.Transparent
            )
        ) {
            Text(
                text = "Login with Google",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Sign up link
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "New here? ",
                fontSize = 14.sp,
                color = TextSecondary
            )
            Text(
                text = "Create an account",
                fontSize = 14.sp,
                color = ZomatoRed,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable {
                    navController.navigate(Routes.SignUpScreen)
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}