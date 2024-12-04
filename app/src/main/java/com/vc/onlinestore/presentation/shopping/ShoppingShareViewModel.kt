package com.vc.onlinestore.presentation.shopping

import androidx.lifecycle.ViewModel
import com.vc.onlinestore.domain.model.CartProduct
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ShoppingShareViewModel @Inject constructor() : ViewModel() {

    private val _cartProducts = MutableStateFlow<List<CartProduct>>(emptyList())
    val cartProducts = _cartProducts.asStateFlow()

    fun setCartProducts(cartProducts: List<CartProduct>) {
        _cartProducts.value = cartProducts
    }
}