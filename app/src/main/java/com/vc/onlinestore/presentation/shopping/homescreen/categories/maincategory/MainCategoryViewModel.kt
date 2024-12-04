package com.vc.onlinestore.presentation.shopping.homescreen.categories.maincategory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vc.onlinestore.domain.usecases.network.GetBestDealsProducts
import com.vc.onlinestore.domain.usecases.network.GetBestProducts
import com.vc.onlinestore.domain.usecases.network.GetSpecialProducts
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
    private val getBestProducts: GetBestProducts
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
            MainCategoryEvent.GetAllProducts -> fetchSpecialProducts()
        }
    }

    private fun fetchSpecialProducts() {
        viewModelScope.launch {
            _specialProductsState.value = ProductsCategoryState(null, true, null)
            when (val result = getSpecialProducts()) {
                is Resource.Error -> {
                    _specialProductsState.value = ProductsCategoryState(null, false, result.message!!)
                }

                is Resource.Success -> {
                    _specialProductsState.value = ProductsCategoryState(result.data, false, null)
                }
            }
            _specialProductsState.value = ProductsCategoryState()
        }

    }

    private fun fetchBestDealsProducts() {
        viewModelScope.launch {
            _bestDealsProductsState.value = ProductsCategoryState(null, true, null)
            when (val result = getBestDealsProducts()) {
                is Resource.Error -> {
                    _bestDealsProductsState.value = ProductsCategoryState(null, false, result.message!!)
                }

                is Resource.Success -> {
                    _bestDealsProductsState.value = ProductsCategoryState(result.data!!, false, null)
                }
            }
            _bestDealsProductsState.value = ProductsCategoryState()
        }
    }

    private fun fetchBestProducts() {
        viewModelScope.launch {
            _bestProductsState.value = ProductsCategoryState(null, true, null)
            when (val result = getBestProducts()) {
                is Resource.Error -> {
                    _bestProductsState.value = ProductsCategoryState(null, false, result.message!!)
                }

                is Resource.Success -> {
                    _bestProductsState.value = ProductsCategoryState(result.data, false, null)
                }
            }
            _bestProductsState.value = ProductsCategoryState()
        }
    }


}