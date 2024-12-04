package com.vc.onlinestore.presentation.shopping.profilescreen.billingscreen.orderscreen

import com.vc.onlinestore.data.firebase.dto.order.Order

sealed class OrderEvent {
    data class PlaceOrder(val order: Order) : OrderEvent()
}