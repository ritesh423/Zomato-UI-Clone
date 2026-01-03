package com.riteshapps.zomatoclone.presentation.screens.CatergoryScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.riteshapps.zomatoclone.R
import com.riteshapps.zomatoclone.presentation.components.FilterRow
import com.riteshapps.zomatoclone.presentation.components.HomeScreenCards

@Composable
fun ChineseCategoryCards(navController: NavController) {
    val LazyListState = rememberLazyListState()
    val filters = listOf("Filter", "Flash Sale", "Under 30 minutes", "Rating", "Schedule")
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        FilterRow(filters)

        Text(
            text = "RECOMMENDED FOR YOU",
            style = TextStyle(
                fontSize = 14.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Normal,
                letterSpacing = 2.sp,
                fontFamily = FontFamily.SansSerif
            ),
            modifier = Modifier
                .padding(vertical = 16.dp)
                .padding(start = 10.dp),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(18.dp))

        Row(
            modifier = Modifier.padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            Column {
                Text(
                    text = "553 RESTAURANTS DELIVERING TO YOU",
                    color = Color.Gray
                )
                Text(
                    text = "Featured",
                    modifier = Modifier.padding(start = 5.dp),
                    color = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        HomeScreenCards(navController = navController)

        Spacer(modifier = Modifier.height(16.dp))
        HomeScreenCards(navController)

        Spacer(modifier = Modifier.height(16.dp))
        HomeScreenCards(navController)

        Spacer(modifier = Modifier.height(16.dp))
        HomeScreenCards(navController)
    }
}

@Composable
fun CategoryCard(
    foodimagebackground1: Int,
    textblackone1: String,
    cardtextname1: String,
    timingtext1: String,
    timerimage1: Int,
    foodimagebackground2: Int,
    textblackone2: String,
    cardtextname2: String,
    timingtext2: String,
    timerimage2: Int
) {

    Column(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .background(Color.White)
    ) {

        Card(
            modifier = Modifier
                .width(140.dp)
                .height(100.dp)
                .padding(horizontal = 2.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {

            Box(modifier = Modifier.fillMaxSize()) {

                Image(
                    painter = painterResource(id = foodimagebackground1),
                    contentDescription = "Burger",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                Text(
                    text = textblackone1,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(bottom = 8.dp)
                        .background(Color.Black.copy(alpha = 0.80f)),
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Icon(
                    painter = painterResource(R.drawable.outline_bookmark_24),
                    contentDescription = "bookmark",
                    modifier = Modifier
                        .size(30.dp)
                        .align(Alignment.TopEnd)
                        .padding(8.dp),
                    tint = Color.White
                )
            }
        }

        Column {
            Text(
                text = cardtextname1,
                modifier = Modifier.padding(start = 10.dp),
                fontSize = 14.sp,
                color = Color.Black,
                fontWeight = FontWeight.SemiBold
            )

            Row(
                modifier = Modifier.padding(start = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.timer),
                    contentDescription = "timer",
                    modifier = Modifier.size(14.dp),
                    tint = Color.Green
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = timingtext1,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Card(
            modifier = Modifier
                .width(140.dp)
                .height(100.dp)
                .padding(horizontal = 2.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {

            Box(modifier = Modifier.fillMaxSize()) {

                Image(
                    painter = painterResource(id = foodimagebackground2),
                    contentDescription = "Burger",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                Text(
                    text = textblackone2,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(bottom = 8.dp)
                        .background(Color.Black.copy(alpha = 0.80f)),
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Icon(
                    painter = painterResource(R.drawable.outline_bookmark_24),
                    contentDescription = "bookmark",
                    modifier = Modifier
                        .size(30.dp)
                        .align(Alignment.TopEnd)
                        .padding(8.dp),
                    tint = Color.White
                )
            }
        }

        Column {
            Text(
                text = cardtextname2,
                modifier = Modifier.padding(start = 10.dp),
                fontSize = 14.sp,
                color = Color.Black,
                fontWeight = FontWeight.SemiBold
            )

            Row(
                modifier = Modifier.padding(start = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = timerimage2),
                    contentDescription = "timer",
                    modifier = Modifier.size(14.dp),
                    tint = Color.Green
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = timingtext2,
                    fontSize = 12.sp,
                    color = Color.LightGray
                )
            }
        }
    }
}
