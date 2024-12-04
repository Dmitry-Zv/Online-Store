package com.vc.onlinestore.presentation.shopping.profilescreen.billingscreen.orderscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vc.onlinestore.data.firebase.dto.order.Order
import com.vc.onlinestore.domain.usecases.firebase.PlaceOrder
import com.vc.onlinestore.domain.usecases.local.DeleteAllCartProducts
import com.vc.onlinestore.presentation.common.Event
import com.vc.onlinestore.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val placeOrder: PlaceOrder,
    private val deleteAllCartProducts: DeleteAllCartProducts
) : ViewModel(), Event<OrderEvent> {


    private val _orderState = MutableStateFlow(OrderState())
    val orderState = _orderState.asStateFlow()

    override fun onEvent(event: OrderEvent) {
        when (event) {
            is OrderEvent.PlaceOrder -> getPlaceOrder(order = event.order)
        }
    }

    private fun getPlaceOrder(order: Order) {
        viewModelScope.launch {
            _orderState.value = OrderState(null, true, null)
            when (val result = placeOrder(order)) {
                is Resource.Error -> {
                    _orderState.value = OrderState(null, false, result.message)
                }

                is Resource.Success -> {
                    deleteAllCartProducts()
                    _orderState.value = OrderState(result.data, false, null)
                }
            }
        }
    }
}