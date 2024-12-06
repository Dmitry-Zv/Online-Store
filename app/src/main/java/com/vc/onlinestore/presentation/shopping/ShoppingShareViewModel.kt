package com.vc.onlinestore.presentation.shopping

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vc.onlinestore.domain.model.CartProduct
import com.vc.onlinestore.domain.usecases.local.GetAllCartProducts
import com.vc.onlinestore.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingShareViewModel @Inject constructor(
    private val getAllCartProducts: GetAllCartProducts
) : ViewModel() {

    private val _cartProducts = MutableStateFlow<List<CartProduct>>(emptyList())
    val cartProducts = _cartProducts.asStateFlow()

    private val _errorState = MutableSharedFlow<String>()
    val errorState = _errorState.asSharedFlow()

    init {
        getCartProducts()
    }

    private fun getCartProducts() {
        viewModelScope.launch {
            when (val result = getAllCartProducts()) {
                is Resource.Error -> {
                    _errorState.emit(result.message!!)
                }

                is Resource.Success -> {
                    _cartProducts.value = result.data!!
                }
            }
        }
    }

    fun setCartProducts(cartProducts: List<CartProduct>) {
        _cartProducts.value = cartProducts
    }
}