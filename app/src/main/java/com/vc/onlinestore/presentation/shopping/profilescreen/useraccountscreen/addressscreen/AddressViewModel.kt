package com.vc.onlinestore.presentation.shopping.profilescreen.useraccountscreen.addressscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vc.onlinestore.data.firebase.dto.Address
import com.vc.onlinestore.domain.usecases.firebase.DeleteAddress
import com.vc.onlinestore.domain.usecases.firebase.SaveAddress
import com.vc.onlinestore.presentation.common.Event
import com.vc.onlinestore.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val saveAddress: SaveAddress,
    private val deleteAddress: DeleteAddress
) : ViewModel(), Event<AddressEvent> {

    private val _addressState = MutableStateFlow(AddressState())
    val addressState = _addressState.asStateFlow()

    private val _deleteAddressState = MutableStateFlow(AddressState())
    val deleteAddressState = _deleteAddressState.asStateFlow()

    private val _error = MutableSharedFlow<String>()
    val error = _error.asSharedFlow()

    override fun onEvent(event: AddressEvent) {
        when (event) {
            is AddressEvent.SaveAddress -> performSavingAddress(address = event.address)
            is AddressEvent.DeleteAddress -> performingDeleteAddress(address = event.address)
        }
    }

    private fun performSavingAddress(address: Address) {
        viewModelScope.launch {
            if (validateInputs(address)) {
                _addressState.value = AddressState(null, true, null)
                when (val result = saveAddress(address)) {
                    is Resource.Error -> {
                        _addressState.value = AddressState(null, false, result.message!!)
                    }

                    is Resource.Success -> {
                        _addressState.value = AddressState(address, false, null)
                    }
                }
            } else {
                _error.emit("All fields are required")
            }
        }
    }

    private fun performingDeleteAddress(address: Address) {
        viewModelScope.launch {
            _deleteAddressState.value = AddressState(null, true, null)
            when (val result = deleteAddress(address)) {
                is Resource.Error -> {
                    _deleteAddressState.value = AddressState(null, false, result.message!!)
                }

                is Resource.Success -> {
                    _deleteAddressState.value = AddressState(address, false, null)
                }
            }
        }
    }

    private fun validateInputs(address: Address): Boolean =
        address.addressTitle.isNotBlank() &&
                address.state.isNotBlank() &&
                address.city.isNotBlank() &&
                address.street.isNotBlank() &&
                address.phone.isNotBlank() &&
                address.fullName.isNotBlank()
}