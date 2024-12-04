package com.vc.onlinestore.presentation.shopping.profilescreen.billingscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vc.onlinestore.domain.usecases.firebase.GetUserAddresses
import com.vc.onlinestore.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BillingViewModel @Inject constructor(
    private val getUserAddresses: GetUserAddresses
) : ViewModel() {

    private val _addressesState = MutableStateFlow(BillingAddressesState())
    val addressesState = _addressesState.asStateFlow()

    init {
        getAddressesForUser()
    }


    fun getAddressesForUser() {
        viewModelScope.launch {
            _addressesState.value = BillingAddressesState(
                null, true, null
            )
            when (val result = getUserAddresses()) {
                is Resource.Error -> {
                    _addressesState.value = BillingAddressesState(
                        null, false, result.message!!
                    )
                }

                is Resource.Success -> {
                    _addressesState.value = BillingAddressesState(
                        result.data, false, null
                    )
                }
            }
        }
    }
}