package com.riteshapps.zomatoclone.presentation.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.riteshapps.zomatoclone.data.local.entity.OrderEntity
import com.riteshapps.zomatoclone.data.local.entity.OrderItemEntity
import com.riteshapps.zomatoclone.presentation.components.ZomatoOutlinedButton
import com.riteshapps.zomatoclone.presentation.components.ZomatoPrimaryButton
import com.riteshapps.zomatoclone.presentation.navigation.Routes
import com.riteshapps.zomatoclone.presentation.viewmodel.OrderViewModel
import com.riteshapps.zomatoclone.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(
    navController: NavController,
    orderViewModel: OrderViewModel = hiltViewModel()
) {
    val orders by orderViewModel.orders.collectAsState()
    val isLoading = orders.isEmpty()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Orders") },
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
        if (orders.isEmpty()) {
            // Empty state
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Receipt,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(80.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No orders yet",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Place your first order and it will appear here",
                        fontSize = 14.sp,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    ZomatoPrimaryButton(
                        text = "Start Ordering",
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.width(200.dp)
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = orders,
                    key = { it.orderId }
                ) { order ->
                    var orderItems by remember { mutableStateOf<List<OrderItemEntity>>(emptyList()) }
                    
                    LaunchedEffect(order.orderId) {
                        orderViewModel.getOrderItems(order.orderId).collect { items ->
                            orderItems = items
                        }
                    }
                    
                    OrderCard(
                        order = order,
                        items = orderItems,
                        onReorder = {
                            navController.navigate(
                                Routes.RestaurantDetailScreen(order.restaurantId)
                            )
                        },
                        onRate = { /* Show rating dialog */ }
                    )
                }
            }
        }
    }
}

@Composable
private fun OrderCard(
    order: OrderEntity,
    items: List<OrderItemEntity>,
    onReorder: () -> Unit,
    onRate: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val dateFormat = remember { SimpleDateFormat("MMM dd, yyyy • hh:mm a", Locale.getDefault()) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = order.restaurantName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = dateFormat.format(Date(order.timestamp)),
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
                
                StatusBadge(status = order.status)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Items preview
            val displayItems = if (expanded) items else items.take(2)
            displayItems.forEach { item ->
                Text(
                    text = "${item.itemName} × ${item.quantity}",
                    fontSize = 13.sp,
                    color = TextSecondary
                )
            }

            if (items.size > 2 && !expanded) {
                Text(
                    text = "and ${items.size - 2} more items",
                    fontSize = 13.sp,
                    color = ZomatoRed,
                    modifier = Modifier.clickable { expanded = true }
                )
            } else if (items.size > 2 && expanded) {
                Text(
                    text = "Show less",
                    fontSize = 13.sp,
                    color = ZomatoRed,
                    modifier = Modifier.clickable { expanded = false }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Total
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Total",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "₹${order.totalAmount}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onReorder,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, ZomatoRed)
                ) {
                    Text("Reorder", color = ZomatoRed)
                }
                
                OutlinedButton(
                    onClick = onRate,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, BorderLight)
                ) {
                    Text("Rate", color = TextSecondary)
                }
            }
        }
    }
}

@Composable
private fun StatusBadge(status: String) {
    val (backgroundColor, textColor) = when (status.lowercase()) {
        "delivered" -> Pair(RatingGreen.copy(alpha = 0.1f), RatingGreen)
        "processing" -> Pair(RatingYellow.copy(alpha = 0.1f), RatingYellow)
        "cancelled" -> Pair(ZomatoRed.copy(alpha = 0.1f), ZomatoRed)
        else -> Pair(DiscountBlue.copy(alpha = 0.1f), DiscountBlue)
    }

    Box(
        modifier = Modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(4.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = status,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = textColor
        )
    }
}
