package com.vc.onlinestore.presentation.shopping.profilescreen.useraccountscreen.addressscreen

import com.vc.onlinestore.data.firebase.dto.Address

data class AddressState(
    val address: Address? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)