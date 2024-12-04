package com.vc.onlinestore.presentation.shopping.profilescreen.useraccountscreen.addressscreen

import com.vc.onlinestore.data.firebase.dto.Address

sealed class AddressEvent {
    data class SaveAddress(val address: Address) : AddressEvent()
    data class DeleteAddress(val address: Address) : AddressEvent()
}