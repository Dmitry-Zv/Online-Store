package com.vc.onlinestore.presentation.shopping.homescreen.categories.maincategory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vc.onlinestore.domain.usecases.network.GetBestDealsProducts
import com.vc.onlinestore.domain.usecases.network.GetBestProducts
import com.vc.onlinestore.domain.usecases.network.GetSpecialProducts
import com.vc.onlinestore.domain.usecases.network.GetToken
import com.vc.onlinestore.presentation.common.Event
import com.vc.onlinestore.presentation.shopping.homescreen.categories.ProductsCategoryState
import com.vc.onlinestore.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainCategoryViewModel @Inject constructor(
    private val getSpecialProducts: GetSpecialProducts,
    private val getBestDealsProducts: GetBestDealsProducts,
    private val getBestProducts: GetBestProducts,
    private val getToken: GetToken
) : ViewModel(), Event<MainCategoryEvent> {

    private val _specialProductsState = MutableStateFlow(ProductsCategoryState())
    val specialProductsState = _specialProductsState.asStateFlow()
    private val _bestDealsProductsState = MutableStateFlow(ProductsCategoryState())
    val bestDealsProductsState = _bestDealsProductsState.asStateFlow()
    private val _bestProductsState = MutableStateFlow(ProductsCategoryState())
    val bestProductsState = _bestProductsState.asStateFlow()

    init {
        fetchSpecialProducts()
        fetchBestDealsProducts()
        fetchBestProducts()
    }

    override fun onEvent(event: MainCategoryEvent) {
        when (event) {
            MainCategoryEvent.BestDealsProducts -> fetchBestDealsProducts()
            MainCategoryEvent.BestProducts -> fetchBestProducts()
            MainCategoryEvent.SpecialProducts -> fetchSpecialProducts()
        }
    }

    private fun fetchSpecialProducts() {
        viewModelScope.launch {
            _specialProductsState.value = ProductsCategoryState(null, true, null)
            val token = getToken()
            when (val result = getSpecialProducts(token)) {
                is Resource.Error -> {
                    _specialProductsState.value =
                        ProductsCategoryState(null, false, result.message!!)
                }

                is Resource.Success -> {
                    _specialProductsState.value = ProductsCategoryState(result.data, false, null)
                }
            }
        }

    }

    private fun fetchBestDealsProducts() {
        viewModelScope.launch {
            _bestDealsProductsState.value = ProductsCategoryState(null, true, null)
            val token = getToken()
            when (val result = getBestDealsProducts(token)) {
                is Resource.Error -> {
                    _bestDealsProductsState.value =
                        ProductsCategoryState(null, false, result.message!!)
                }

                is Resource.Success -> {
                    _bestDealsProductsState.value =
                        ProductsCategoryState(result.data!!, false, null)
                }
            }
        }
    }

    private fun fetchBestProducts() {
        viewModelScope.launch {
            _bestProductsState.value = ProductsCategoryState(null, true, null)
            val token = getToken()
            when (val result = getBestProducts(token)) {
                is Resource.Error -> {
                    _bestProductsState.value = ProductsCategoryState(null, false, result.message!!)
                }

                is Resource.Success -> {
                    _bestProductsState.value = ProductsCategoryState(result.data, false, null)
                }
            }
        }
    }


}