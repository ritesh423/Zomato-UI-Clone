package com.riteshapps.zomatoclone.presentation.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.riteshapps.zomatoclone.presentation.components.ZomatoOutlinedButton
import com.riteshapps.zomatoclone.presentation.components.ZomatoPrimaryButton
import com.riteshapps.zomatoclone.presentation.navigation.Routes
import com.riteshapps.zomatoclone.presentation.navigation.SubNavigation
import com.riteshapps.zomatoclone.ui.theme.RatingGreen
import com.riteshapps.zomatoclone.ui.theme.TextSecondary

@Composable
fun OrderSuccessScreen(
    navController: NavController
) {
    val orderId = remember { "ORD${System.currentTimeMillis().toString().takeLast(8)}" }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Animated checkmark
        AnimatedCheckmark(
            modifier = Modifier.size(120.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Order Placed!",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Order ID pill
        Box(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = "Order ID: $orderId",
                fontSize = 13.sp,
                color = TextSecondary
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Delivery estimate
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(
                    color = RatingGreen.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Schedule,
                contentDescription = null,
                tint = RatingGreen,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Estimated delivery: 30-45 mins",
                fontSize = 14.sp,
                color = RatingGreen,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(48.dp))

        // Action buttons
        ZomatoOutlinedButton(
            text = "Go to Orders",
            onClick = {
                navController.navigate(Routes.OrdersScreen) {
                    popUpTo(SubNavigation.MainHomeScreen) { inclusive = false }
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        ZomatoPrimaryButton(
            text = "Back to Home",
            onClick = {
                navController.navigate(Routes.DeliveryScreen) {
                    popUpTo(SubNavigation.MainHomeScreen) { inclusive = false }
                }
            }
        )
    }
}

@Composable
private fun AnimatedCheckmark(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "checkmark")
    
    var animationPlayed by remember { mutableStateOf(false) }
    val progress by animateFloatAsState(
        targetValue = if (animationPlayed) 1f else 0f,
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing),
        label = "progress"
    )

    LaunchedEffect(Unit) {
        animationPlayed = true
    }

    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(RatingGreen.copy(alpha = 0.1f)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .size(80.dp)
                .padding(16.dp)
        ) {
            val strokeWidth = 8.dp.toPx()
            val checkmarkPath = androidx.compose.ui.graphics.Path().apply {
                moveTo(size.width * 0.2f, size.height * 0.5f)
                lineTo(size.width * 0.4f, size.height * 0.7f)
                lineTo(size.width * 0.8f, size.height * 0.3f)
            }

            // Draw circle
            drawCircle(
                color = RatingGreen,
                radius = (size.minDimension / 2) * progress,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // Draw checkmark
            if (progress > 0.5f) {
                val checkProgress = ((progress - 0.5f) * 2f).coerceIn(0f, 1f)
                
                // First line of checkmark
                val line1EndX = size.width * 0.2f + (size.width * 0.2f) * checkProgress.coerceAtMost(0.5f) * 2
                val line1EndY = size.height * 0.5f + (size.height * 0.2f) * checkProgress.coerceAtMost(0.5f) * 2
                
                drawLine(
                    color = RatingGreen,
                    start = Offset(size.width * 0.2f, size.height * 0.5f),
                    end = Offset(line1EndX, line1EndY),
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round
                )

                // Second line of checkmark
                if (checkProgress > 0.5f) {
                    val line2Progress = (checkProgress - 0.5f) * 2
                    val line2EndX = size.width * 0.4f + (size.width * 0.4f) * line2Progress
                    val line2EndY = size.height * 0.7f - (size.height * 0.4f) * line2Progress
                    
                    drawLine(
                        color = RatingGreen,
                        start = Offset(size.width * 0.4f, size.height * 0.7f),
                        end = Offset(line2EndX, line2EndY),
                        strokeWidth = strokeWidth,
                        cap = StrokeCap.Round
                    )
                }
            }
        }
    }
}
