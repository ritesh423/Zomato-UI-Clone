package com.riteshapps.zomatoclone.presentation.screens.CatergoryScreen

import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun PizzaCategoryScreen(navController: NavController) {
    ChineseCategoryCards(navController = navController)
}