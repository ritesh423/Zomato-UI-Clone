package com.riteshapps.zomatoclone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import com.riteshapps.zomatoclone.data.local.DatabaseSeeder
import com.riteshapps.zomatoclone.presentation.navigation.App
import com.riteshapps.zomatoclone.presentation.viewmodel.ProfileViewModel
import com.riteshapps.zomatoclone.ui.theme.ZomatoCloneTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    @Inject
    lateinit var databaseSeeder: DatabaseSeeder

    override fun onCreate(savedInstanceState: Bundle?) {
        // Install splash screen before super.onCreate
        installSplashScreen()
        
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Seed database on first launch
        lifecycleScope.launch {
            databaseSeeder.seedIfEmpty()
        }
        
        setContent {
            ZomatoApp()
        }
    }
}

@Composable
fun ZomatoApp() {
    val profileViewModel: ProfileViewModel = hiltViewModel()
    val isDarkMode by profileViewModel.darkMode.collectAsState()
    
    ZomatoCloneTheme(darkTheme = isDarkMode) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            App()
        }
    }
}