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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.riteshapps.zomatoclone.common.UiState
import com.riteshapps.zomatoclone.data.local.entity.AddressEntity
import com.riteshapps.zomatoclone.presentation.components.*
import com.riteshapps.zomatoclone.presentation.navigation.Routes
import com.riteshapps.zomatoclone.presentation.viewmodel.CartViewModel
import com.riteshapps.zomatoclone.presentation.viewmodel.CheckoutViewModel
import com.riteshapps.zomatoclone.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    navController: NavController,
    checkoutViewModel: CheckoutViewModel = hiltViewModel(),
    cartViewModel: CartViewModel = hiltViewModel()
) {
    val addresses by checkoutViewModel.addresses.collectAsState()
    val selectedAddressId by checkoutViewModel.selectedAddressId.collectAsState()
    val selectedPaymentMethod by checkoutViewModel.selectedPaymentMethod.collectAsState()
    val orderState by checkoutViewModel.orderState.collectAsState()
    
    val cartItems by cartViewModel.cartItems.collectAsState()
    val grandTotal by cartViewModel.grandTotal.collectAsState()
    val deliveryFee by cartViewModel.deliveryFee.collectAsState()
    val discount by cartViewModel.discount.collectAsState()
    val restaurantId by cartViewModel.cartRestaurantId.collectAsState()
    val restaurantName by cartViewModel.restaurantName.collectAsState()
    
    var showAddAddressDialog by remember { mutableStateOf(false) }

    // Handle order success
    LaunchedEffect(orderState) {
        if (orderState is UiState.Success) {
            cartViewModel.clearCart()
            navController.navigate(Routes.OrderSuccessScreen) {
                popUpTo(Routes.CartScreen) { inclusive = true }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Checkout") },
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
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(bottom = 80.dp)
            ) {
                // Delivery Address Section
                item {
                    SectionHeader(title = "Delivery Address")
                }

                if (addresses.isEmpty()) {
                    item {
                        Text(
                            text = "No addresses found. Add one below.",
                            color = TextSecondary,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                } else {
                    items(
                        items = addresses,
                        key = { it.id }
                    ) { address ->
                        AddressCard(
                            label = address.label,
                            address = address.fullAddress,
                            isDefault = address.isDefault,
                            isSelected = address.id == selectedAddressId,
                            onSelect = { checkoutViewModel.selectAddress(address.id) },
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                        )
                    }
                }

                // Add new address
                item {
                    OutlinedButton(
                        onClick = { showAddAddressDialog = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, ZomatoRed)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = ZomatoRed
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Add New Address",
                            color = ZomatoRed
                        )
                    }
                }

                // Payment Method Section
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    SectionHeader(title = "Payment Method")
                }

                item {
                    PaymentMethodSection(
                        selectedMethod = selectedPaymentMethod,
                        onMethodSelect = { checkoutViewModel.selectPaymentMethod(it) }
                    )
                }

                // Order Summary Section
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    SectionHeader(title = "Order Summary")
                }

                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            cartItems.forEach { item ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "${item.itemName} × ${item.quantity}",
                                        fontSize = 13.sp,
                                        color = TextSecondary,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Text(
                                        text = "₹${item.itemPrice * item.quantity}",
                                        fontSize = 13.sp
                                    )
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            HorizontalDivider(color = DividerColor)
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Total",
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

            // Place Order Button
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                shadowElevation = 8.dp
            ) {
                ZomatoPrimaryButton(
                    text = "Place Order • ₹$grandTotal",
                    onClick = {
                        val restId = restaurantId ?: return@ZomatoPrimaryButton
                        checkoutViewModel.placeOrder(
                            cartItems = cartItems,
                            restaurantId = restId,
                            restaurantName = restaurantName,
                            deliveryFee = deliveryFee,
                            discount = discount
                        )
                    },
                    enabled = selectedAddressId != null && cartItems.isNotEmpty(),
                    isLoading = orderState is UiState.Loading,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }

    // Add Address Dialog
    if (showAddAddressDialog) {
        AddAddressDialog(
            onDismiss = { showAddAddressDialog = false },
            onSave = { label, address, landmark ->
                checkoutViewModel.addAddress(label, address, landmark)
                showAddAddressDialog = false
            }
        )
    }
}

@Composable
private fun PaymentMethodSection(
    selectedMethod: String?,
    onMethodSelect: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column {
            PaymentMethodRow(
                icon = Icons.Default.Money,
                title = "Cash on Delivery",
                isSelected = selectedMethod == "COD",
                onClick = { onMethodSelect("COD") }
            )
            HorizontalDivider(color = DividerColor)
            PaymentMethodRow(
                icon = Icons.Default.QrCode,
                title = "UPI",
                isSelected = selectedMethod == "UPI",
                onClick = { onMethodSelect("UPI") }
            )
            HorizontalDivider(color = DividerColor)
            PaymentMethodRow(
                icon = Icons.Default.CreditCard,
                title = "Debit/Credit Card",
                isSelected = selectedMethod == "CARD",
                onClick = { onMethodSelect("CARD") }
            )
        }
    }
}

@Composable
private fun PaymentMethodRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isSelected) ZomatoRed else TextSecondary
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            fontSize = 14.sp,
            modifier = Modifier.weight(1f)
        )
        RadioButton(
            selected = isSelected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = ZomatoRed
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddAddressDialog(
    onDismiss: () -> Unit,
    onSave: (String, String, String) -> Unit
) {
    var label by remember { mutableStateOf("Home") }
    var address by remember { mutableStateOf("") }
    var landmark by remember { mutableStateOf("") }
    var addressError by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Address") },
        text = {
            Column {
                // Label selection
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("Home", "Work", "Other").forEach { option ->
                        FilterChip(
                            selected = label == option,
                            onClick = { label = option },
                            label = { Text(option) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = ZomatoRedLight,
                                selectedLabelColor = ZomatoRed
                            )
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedTextField(
                    value = address,
                    onValueChange = {
                        address = it
                        addressError = null
                    },
                    label = { Text("Full Address") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = addressError != null,
                    supportingText = addressError?.let { { Text(it) } },
                    minLines = 3,
                    maxLines = 5
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = landmark,
                    onValueChange = { landmark = it },
                    label = { Text("Landmark (Optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (address.isBlank()) {
                        addressError = "Please enter an address"
                    } else {
                        onSave(label, address, landmark)
                    }
                }
            ) {
                Text("Save", color = ZomatoRed)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
