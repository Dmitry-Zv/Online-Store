package com.vc.onlinestore.presentation.shopping.cartscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vc.onlinestore.domain.model.CartProduct
import com.vc.onlinestore.domain.usecases.local.DeleteCartProduct
import com.vc.onlinestore.domain.usecases.local.GetAllCartProducts
import com.vc.onlinestore.domain.usecases.local.InsertCartProduct
import com.vc.onlinestore.helper.getProductPrice
import com.vc.onlinestore.presentation.common.CartProductsState
import com.vc.onlinestore.presentation.common.Event
import com.vc.onlinestore.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val getAllCartProducts: GetAllCartProducts,
    private val insertCartProduct: InsertCartProduct,
    private val deleteCartProduct: DeleteCartProduct
) : ViewModel(), Event<CartEvent> {

    private val _cartProductsState = MutableStateFlow(CartProductsState())
    val cartProductsState = _cartProductsState.asStateFlow()

    private val _deleteDialog = MutableSharedFlow<CartProduct>()
    val deleteDialog = _deleteDialog.asSharedFlow()


    val productPrice = cartProductsState.map {
        if (it.product != null) {
            calculatePrice(it.product)
        } else {
            null
        }
    }

    private fun calculatePrice(products: List<CartProduct>): Float =
        products.sumByDouble { cartProduct ->
            (cartProduct.product.offerPercentage.getProductPrice(cartProduct.product.price) * cartProduct.quantity).toDouble()
        }.toFloat()

    init {
        getCartProducts()
    }

    override fun onEvent(event: CartEvent) {
        when (event) {
            is CartEvent.DecreaseQuantity -> changeProduct(
                cartProduct = event.cartProduct.copy(
                    quantity = event.cartProduct.quantity - 1
                )
            )

            is CartEvent.IncreaseQuantity -> changeProduct(
                cartProduct = event.cartProduct.copy(
                    quantity = event.cartProduct.quantity + 1
                )
            )

            is CartEvent.DeleteCartProduct -> deleteProduct(cartProduct = event.cartProduct)
        }
    }

    private fun getCartProducts() {
        viewModelScope.launch {
            _cartProductsState.value = CartProductsState(null, true, null)
            when (val result = getAllCartProducts()) {
                is Resource.Error -> {
                    _cartProductsState.value = CartProductsState(null, false, result.message!!)
                }

                is Resource.Success -> {
                    _cartProductsState.value = CartProductsState(result.data, false, null)
                }
            }
        }
    }

    private fun changeProduct(cartProduct: CartProduct) {
        viewModelScope.launch {
            if (cartProduct.quantity == 0) {
                _deleteDialog.emit(cartProduct)
                return@launch
            }
            _cartProductsState.value = CartProductsState(null, true, null)
            insertCartProduct(cartProduct)
            updateProducts()
        }
    }

    private fun deleteProduct(cartProduct: CartProduct) {
        viewModelScope.launch {
            _cartProductsState.value = CartProductsState(null, true, null)
            deleteCartProduct(cartProduct)
            updateProducts()
        }
    }

    private suspend fun updateProducts() {
        when (val result = getAllCartProducts()) {
            is Resource.Error -> {
                _cartProductsState.value = CartProductsState(null, false, result.message!!)
            }

            is Resource.Success -> {
                _cartProductsState.value = CartProductsState(result.data, false, null)
            }
        }
    }
}