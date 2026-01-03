import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.bottombar.AnimatedBottomBar
import com.riteshapps.zomatoclone.R
import com.riteshapps.zomatoclone.presentation.navigation.Routes
import com.riteshapps.zomatoclone.presentation.navigation.SubNavigation
import com.riteshapps.zomatoclone.presentation.screens.DeliveryScreen
import com.riteshapps.zomatoclone.presentation.screens.DiningScreen
import com.riteshapps.zomatoclone.presentation.screens.LoginScreen
import com.riteshapps.zomatoclone.presentation.screens.ProfileScreen
import com.riteshapps.zomatoclone.presentation.screens.QuickScreen
import com.riteshapps.zomatoclone.presentation.screens.SignUpScreen

data class BottomNavItem(
    val title: String,
    val icon: Painter
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(
    isVisible: Boolean,
    listState: LazyListState
) {

    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route

    var shouldShowBottomBar by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(currentDestination) {
        shouldShowBottomBar = when (currentDestination) {
            Routes.DeliveryScreen::class.qualifiedName,
            Routes.QuickScreen::class.qualifiedName,
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
            title = "Quick",
            icon = painterResource(R.drawable.quick_icon)
        ),
        BottomNavItem(
            title = "Dining",
            icon = painterResource(R.drawable.dining)
        )
    )

    val selectedColor = colorResource(R.color.purple_500)

    val bottomBarHeight by animateDpAsState(
        targetValue = if (isVisible) 64.dp else 0.dp
    )

    var startScreen = if (true) {
        SubNavigation.LoginSignUpScreen
    } else {
        SubNavigation.MainHomeScreen
    }

    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                modifier = Modifier
                    .padding(
                        WindowInsets.navigationBars
                            .only(WindowInsetsSides.Bottom)
                            .asPaddingValues()
                    )
                    .fillMaxWidth()
                    .height(bottomBarHeight),
                visible = shouldShowBottomBar,
                enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
                exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(bottomBarHeight)
                        .background(Color.White)
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(3.dp)
                            .background(Color.LightGray.copy(alpha = 0.2f))
                    ) {
                        BottomNavItems.forEachIndexed { index, _ ->
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 10.dp)
                                    .weight(1f)
                                    .height(3.dp)
                                    .clip(shape = RoundedCornerShape(100.dp))
                                    .background(
                                        if (index == selectedItemIndex)
                                            selectedColor
                                        else
                                            Color.Transparent
                                    )
                            )
                        }
                    }

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White),
                        shadowElevation = 8.dp
                    ) {

                        AnimatedBottomBar(
                            containerColor = Color.White,
                            animationSpec = spring(
                                dampingRatio = 1f,
                                stiffness = Spring.StiffnessMediumLow
                            )
                        ) {

                            BottomNavItems.forEachIndexed { index, item ->

                                NavigationBarItem(
                                    modifier = Modifier.align(alignment = Alignment.Top),
                                    selected = selectedItemIndex == index,
                                    onClick = {
                                        selectedItemIndex = index
                                        when (selectedItemIndex) {
                                            0 -> navController.navigate(Routes.DeliveryScreen)
                                            1 -> navController.navigate(Routes.QuickScreen)
                                            2 -> navController.navigate(Routes.DiningScreen)
                                        }
                                    },
                                    label = {
                                        if (index == selectedItemIndex) {
                                            Text(
                                                text = item.title,
                                                color = selectedColor,
                                                fontSize = 16.sp
                                            )
                                        } else {
                                            Text(
                                                text = item.title,
                                                color = Color.Gray,
                                                fontSize = 16.sp
                                            )
                                        }
                                    },
                                    icon = {
                                        Icon(
                                            painter = item.icon,
                                            contentDescription = item.title,
                                            tint = if (index == selectedItemIndex)
                                                selectedColor
                                            else
                                                Color.Gray
                                        )
                                    },
                                    colors = NavigationBarItemDefaults.colors(
                                        indicatorColor = Color.Transparent
                                    )
                                )
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
                startDestination = startScreen
            ) {

                navigation<SubNavigation.LoginSignUpScreen>(
                    startDestination = Routes.LoginScreen,
                ) {
                    composable<Routes.LoginScreen> {
                        LoginScreen(
                            navController = navController
                        )
                    }

                    composable<Routes.SignUpScreen> {
                        SignUpScreen(
                            navController = navController
                        )
                    }
                }

                navigation<SubNavigation.MainHomeScreen>(
                    startDestination = Routes.DeliveryScreen
                ) {

                    composable<Routes.DeliveryScreen> {
                        DeliveryScreen(navController, listState)
                    }

                    composable<Routes.QuickScreen> {
                        QuickScreen(navController, listState)
                    }

                    composable<Routes.DiningScreen> {
                        DiningScreen(navController, listState)
                    }

                    composable<Routes.ProfileScreen> {
                        ProfileScreen(navController)
                    }
                }
            }
        }
    }
}
