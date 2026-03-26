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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.riteshapps.zomatoclone.data.local.entity.CartItemEntity
import com.riteshapps.zomatoclone.presentation.components.*
import com.riteshapps.zomatoclone.presentation.navigation.Routes
import com.riteshapps.zomatoclone.presentation.viewmodel.CartViewModel
import com.riteshapps.zomatoclone.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    navController: NavController,
    cartViewModel: CartViewModel = hiltViewModel()
) {
    val cartItems by cartViewModel.cartItems.collectAsState()
    val restaurantName by cartViewModel.restaurantName.collectAsState()
    val itemTotal by cartViewModel.itemTotal.collectAsState()
    val deliveryFee by cartViewModel.deliveryFee.collectAsState()
    val gst by cartViewModel.gst.collectAsState()
    val discount by cartViewModel.discount.collectAsState()
    val grandTotal by cartViewModel.grandTotal.collectAsState()
    val appliedCoupon by cartViewModel.appliedCoupon.collectAsState()
    
    var showCouponSheet by remember { mutableStateOf(false) }
    var couponCode by remember { mutableStateOf("") }
    var couponError by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cart") },
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
        if (cartItems.isEmpty()) {
            // Empty cart state
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
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(80.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Your cart is empty",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Add items to get started",
                        fontSize = 14.sp,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    ZomatoPrimaryButton(
                        text = "Browse Restaurants",
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.width(200.dp)
                    )
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(bottom = 80.dp)
                ) {
                    // Restaurant name
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Restaurant,
                                    contentDescription = null,
                                    tint = ZomatoRed
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = restaurantName,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    // Cart items
                    items(
                        items = cartItems,
                        key = { it.id }
                    ) { cartItem ->
                        CartItemRow(
                            cartItem = cartItem,
                            onIncrement = { cartViewModel.incrementItem(cartItem.menuItemId) },
                            onDecrement = { cartViewModel.decrementItem(cartItem.menuItemId) },
                            onRemove = { cartViewModel.removeItem(cartItem.menuItemId) }
                        )
                        HorizontalDivider(color = DividerColor)
                    }

                    // Add more items link
                    item {
                        TextButton(
                            onClick = { navController.popBackStack() },
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                tint = ZomatoRed,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Add more items",
                                color = ZomatoRed,
                                fontSize = 14.sp
                            )
                        }
                    }

                    // Coupon section
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .clickable { showCouponSheet = true },
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, BorderLight)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocalOffer,
                                    contentDescription = null,
                                    tint = if (appliedCoupon != null) RatingGreen else TextSecondary
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    if (appliedCoupon != null) {
                                        Text(
                                            text = "Coupon applied: $appliedCoupon",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = RatingGreen
                                        )
                                        Text(
                                            text = "You saved ₹$discount",
                                            fontSize = 12.sp,
                                            color = RatingGreen
                                        )
                                    } else {
                                        Text(
                                            text = "Apply coupon",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                        Text(
                                            text = "You have 3 coupons available",
                                            fontSize = 12.sp,
                                            color = TextSecondary
                                        )
                                    }
                                }
                                Icon(
                                    imageVector = Icons.Default.ChevronRight,
                                    contentDescription = null,
                                    tint = TextSecondary
                                )
                            }
                        }
                    }

                    // Bill details
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = "Bill Details",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                
                                Spacer(modifier = Modifier.height(16.dp))
                                
                                BillRow(label = "Item Total", amount = itemTotal)
                                BillRow(
                                    label = "Delivery Fee",
                                    amount = deliveryFee,
                                    originalAmount = if (deliveryFee == 0) 30 else null
                                )
                                BillRow(label = "GST (5%)", amount = gst)
                                
                                if (discount > 0) {
                                    BillRow(
                                        label = "Discount",
                                        amount = -discount,
                                        isDiscount = true
                                    )
                                }
                                
                                Spacer(modifier = Modifier.height(8.dp))
                                HorizontalDivider(color = DividerColor)
                                Spacer(modifier = Modifier.height(8.dp))
                                
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Grand Total",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "₹$grandTotal",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }

                // Checkout button
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter),
                    shadowElevation = 8.dp
                ) {
                    ZomatoPrimaryButton(
                        text = "Proceed to Pay • ₹$grandTotal",
                        onClick = { navController.navigate(Routes.CheckoutScreen) },
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }

    // Coupon bottom sheet
    if (showCouponSheet) {
        ModalBottomSheet(
            onDismissRequest = { showCouponSheet = false }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Apply Coupon",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedTextField(
                    value = couponCode,
                    onValueChange = {
                        couponCode = it.uppercase()
                        couponError = null
                    },
                    label = { Text("Enter coupon code") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = couponError != null,
                    supportingText = couponError?.let { { Text(it) } },
                    trailingIcon = {
                        TextButton(
                            onClick = {
                                val result = cartViewModel.applyCoupon(couponCode)
                                if (result) {
                                    showCouponSheet = false
                                    couponCode = ""
                                } else {
                                    couponError = "Invalid coupon code"
                                }
                            }
                        ) {
                            Text("Apply", color = ZomatoRed)
                        }
                    }
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Available Coupons",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextSecondary
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                CouponOption(
                    code = "SAVE10",
                    description = "10% off on your order",
                    onClick = {
                        cartViewModel.applyCoupon("SAVE10")
                        showCouponSheet = false
                    }
                )
                
                CouponOption(
                    code = "FIRST50",
                    description = "₹50 off on your first order",
                    onClick = {
                        cartViewModel.applyCoupon("FIRST50")
                        showCouponSheet = false
                    }
                )
                
                CouponOption(
                    code = "FREEDEL",
                    description = "Free delivery on this order",
                    onClick = {
                        cartViewModel.applyCoupon("FREEDEL")
                        showCouponSheet = false
                    }
                )
                
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun CartItemRow(
    cartItem: CartItemEntity,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onRemove: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Item image
        AsyncImage(
            model = cartItem.imageResId,
            contentDescription = cartItem.itemName,
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        
        Spacer(modifier = Modifier.width(12.dp))
        
        // Item details
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = cartItem.itemName,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "₹${cartItem.itemPrice}",
                fontSize = 13.sp,
                color = TextSecondary
            )
        }
        
        // Quantity stepper
        QuantityStepper(
            quantity = cartItem.quantity,
            onIncrement = onIncrement,
            onDecrement = onDecrement
        )
        
        Spacer(modifier = Modifier.width(12.dp))
        
        // Item total
        Text(
            text = "₹${cartItem.itemPrice * cartItem.quantity}",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun BillRow(
    label: String,
    amount: Int,
    originalAmount: Int? = null,
    isDiscount: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            color = TextSecondary
        )
        Row {
            if (originalAmount != null) {
                Text(
                    text = "₹$originalAmount",
                    fontSize = 13.sp,
                    color = TextSecondary,
                    textDecoration = TextDecoration.LineThrough
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "FREE",
                    fontSize = 13.sp,
                    color = RatingGreen,
                    fontWeight = FontWeight.Medium
                )
            } else {
                Text(
                    text = if (isDiscount) "-₹${kotlin.math.abs(amount)}" else "₹$amount",
                    fontSize = 13.sp,
                    color = if (isDiscount) RatingGreen else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
private fun CouponOption(
    code: String,
    description: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, BorderLight)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = code,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = ZomatoRed
                )
                Text(
                    text = description,
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }
            Text(
                text = "APPLY",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = ZomatoRed
            )
        }
    }
}
