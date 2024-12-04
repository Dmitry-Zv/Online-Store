package com.vc.onlinestore.presentation.shopping.profilescreen.orderssreen

import com.vc.onlinestore.data.firebase.dto.order.Order

data class OrdersState(
    val orders: List<Order>? = null,
    val isLoading: Boolean = false,
    val message: String? = null
)