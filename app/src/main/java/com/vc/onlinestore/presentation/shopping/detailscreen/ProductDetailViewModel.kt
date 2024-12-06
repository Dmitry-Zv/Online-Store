package com.vc.onlinestore.presentation.shopping.detailscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vc.onlinestore.domain.model.CartProduct
import com.vc.onlinestore.domain.usecases.local.GetAllCartProducts
import com.vc.onlinestore.domain.usecases.local.InsertCartProduct
import com.vc.onlinestore.presentation.common.CartProductsState
import com.vc.onlinestore.presentation.common.Event
import com.vc.onlinestore.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val insertCartProduct: InsertCartProduct,
    private val getAllCartProducts: GetAllCartProducts
) : ViewModel(), Event<ProductDetailsEvent> {

    private val _cartProductsState = MutableStateFlow(CartProductsState())
    val cartProductsCategoryState = _cartProductsState.asStateFlow()


    override fun onEvent(event: ProductDetailsEvent) {
        when (event) {
            is ProductDetailsEvent.AddToCart -> addCartProduct(cartProduct = event.cartProduct)
        }
    }

    private fun addCartProduct(cartProduct: CartProduct) {
        viewModelScope.launch {
            _cartProductsState.value = CartProductsState(null, true, null)
            when (val result = getAllCartProducts()) {
                is Resource.Error -> {
                    _cartProductsState.value =
                        CartProductsState(null, false, result.message!!)
                }

                is Resource.Success -> {
                    if (result.data?.isEmpty() == true) {
                        insertCartProduct(cartProduct)
                    } else {
                        val findCardProduct = CartProduct.findQuantitiesForCartProduct(
                            result.data!!,
                            cartProduct
                        )
                        if (findCardProduct != null) {
                            insertCartProduct(findCardProduct.copy(quantity = findCardProduct.quantity + 1))
                        } else {
                            insertCartProduct(cartProduct)
                        }
                    }
                    getCartProducts()
                }
            }
        }
    }

    private suspend fun getCartProducts() {
        when (val result = getAllCartProducts()) {
            is Resource.Error -> {
                _cartProductsState.value =
                    CartProductsState(null, false, result.message!!)
            }

            is Resource.Success -> {
                _cartProductsState.value =
                    CartProductsState(result.data, false, null)
            }
        }
    }
}