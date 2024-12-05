package com.vc.onlinestore.presentation.shopping.profilescreen.orderssreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vc.onlinestore.domain.usecases.firebase.GetAllOrdersByUser
import com.vc.onlinestore.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val getAllOrdersByUser: GetAllOrdersByUser
) : ViewModel() {

    private val _ordersState = MutableStateFlow(OrdersState())
    val ordersState = _ordersState.asStateFlow()


    fun getAllOrders() {
        viewModelScope.launch {
            _ordersState.value = OrdersState(null, true, null)
            when (val result = getAllOrdersByUser()) {
                is Resource.Error -> {
                    _ordersState.value = OrdersState(null, false, result.message!!)
                }

                is Resource.Success -> {
                    _ordersState.value = OrdersState(result.data!!, false, null)
                }
            }
        }
    }
}