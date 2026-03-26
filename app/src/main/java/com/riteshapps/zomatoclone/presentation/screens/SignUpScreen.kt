package com.riteshapps.zomatoclone.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.riteshapps.zomatoclone.common.UiState
import com.riteshapps.zomatoclone.presentation.components.PasswordStrengthIndicator
import com.riteshapps.zomatoclone.presentation.components.ZomatoPrimaryButton
import com.riteshapps.zomatoclone.presentation.components.ZomatoTextField
import com.riteshapps.zomatoclone.presentation.navigation.Routes
import com.riteshapps.zomatoclone.presentation.navigation.SubNavigation
import com.riteshapps.zomatoclone.presentation.viewmodel.AuthViewModel
import com.riteshapps.zomatoclone.ui.theme.TextSecondary
import com.riteshapps.zomatoclone.ui.theme.ZomatoRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val registerState by authViewModel.registerState.collectAsState()

    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    var nameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }

    // Handle registration success
    LaunchedEffect(registerState) {
        if (registerState is UiState.Success) {
            navController.navigate(SubNavigation.MainHomeScreen) {
                popUpTo(SubNavigation.AuthGraph) { inclusive = true }
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
        // Back button
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier.offset(x = (-12).dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back"
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Title
        Text(
            text = "Create Account",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Join us and discover the best food near you",
            fontSize = 14.sp,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Full Name
        ZomatoTextField(
            value = fullName,
            onValueChange = {
                fullName = it
                nameError = null
            },
            label = "Full Name",
            isError = nameError != null,
            errorMessage = nameError,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = TextSecondary
                )
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Email
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

        // Password
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

        Spacer(modifier = Modifier.height(8.dp))

        PasswordStrengthIndicator(password = password)

        Spacer(modifier = Modifier.height(16.dp))

        // Confirm Password
        ZomatoTextField(
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
                confirmPasswordError = null
            },
            label = "Confirm Password",
            isError = confirmPasswordError != null,
            errorMessage = confirmPasswordError,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = TextSecondary
                )
            },
            trailingIcon = {
                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                    Icon(
                        imageVector = if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = if (confirmPasswordVisible) "Hide password" else "Show password",
                        tint = TextSecondary
                    )
                }
            },
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Error message
        if (registerState is UiState.Error) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = (registerState as UiState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(12.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Create Account button
        ZomatoPrimaryButton(
            text = "Create Account",
            onClick = {
                // Validate
                var isValid = true
                
                if (fullName.isBlank()) {
                    nameError = "Please enter your name"
                    isValid = false
                }
                
                if (email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailError = "Please enter a valid email"
                    isValid = false
                }
                
                if (password.length < 6) {
                    passwordError = "Password must be at least 6 characters"
                    isValid = false
                }
                
                if (password != confirmPassword) {
                    confirmPasswordError = "Passwords do not match"
                    isValid = false
                }
                
                if (isValid) {
                    authViewModel.register(fullName, email, password, confirmPassword)
                }
            },
            isLoading = registerState is UiState.Loading
        )

        Spacer(modifier = Modifier.weight(1f))

        // Login link
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Already have an account? ",
                fontSize = 14.sp,
                color = TextSecondary
            )
            Text(
                text = "Login",
                fontSize = 14.sp,
                color = ZomatoRed,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable {
                    navController.popBackStack()
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}