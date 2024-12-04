package com.vc.onlinestore.presentation.shopping.profilescreen.billingscreen

import com.vc.onlinestore.data.firebase.dto.Address

data class BillingAddressesState(
    val addresses: List<Address>? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)