package com.riteshapps.zomatoclone.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.riteshapps.zomatoclone.R
import com.riteshapps.zomatoclone.data.models.FoodCategoryData

@Composable
fun FoodCategoryTabs(
    modifier: Modifier,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    val categories = listOf(
        FoodCategoryData("All", R.drawable.allfood),
        FoodCategoryData("Pizza", R.drawable.pizza_image),
        FoodCategoryData("chinese", R.drawable.chinese),
        FoodCategoryData("Burgers", R.drawable.burger),
        FoodCategoryData("Biryani", R.drawable.vegbiryani),
        FoodCategoryData("Sweets", R.drawable.sweets),
        FoodCategoryData("Pasta", R.drawable.pasta),
        FoodCategoryData("Rolls", R.drawable.rolls),
        FoodCategoryData("Ice Cream", R.drawable.ice_cream),
    )
    ScrollableTabRow(
        selectedTabIndex = selectedTabIndex,
        containerColor = Color.White,
        contentColor = Color.Black,
        edgePadding = 8.dp,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier
                    .tabIndicatorOffset(tabPositions[selectedTabIndex])
                    .height(3.dp), // Customize thickness if needed
                color = Color.Red
            )
        },
        divider = {
            HorizontalDivider(color = Color.LightGray, thickness = 0.6.dp) // <- customize divider
        }
    ) {
        categories.forEachIndexed { index, category ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = {
                    onTabSelected(index)
                }
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(8.dp)
                ) {
                    AsyncImage(
                        model = category.image,
                        contentDescription = category.name,
                        modifier = Modifier.size(60.dp)
                    )
                    Text(
                        text = category.name,
                        fontSize = 12.sp,
                        fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal,
                        color = if (selectedTabIndex == index) Color.Black else Color.DarkGray
                    )
                }
            }
        }
    }
}