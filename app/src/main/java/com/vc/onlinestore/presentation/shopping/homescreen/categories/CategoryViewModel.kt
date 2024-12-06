package com.vc.onlinestore.presentation.shopping.homescreen.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vc.onlinestore.domain.model.Category
import com.vc.onlinestore.domain.usecases.network.GetBestProductsByCategory
import com.vc.onlinestore.domain.usecases.network.GetOfferProductsByCategory
import com.vc.onlinestore.presentation.common.Event
import com.vc.onlinestore.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class CategoryViewModel(
    private val getOfferProductsByCategory: GetOfferProductsByCategory,
    private val getBestProductsByCategory: GetBestProductsByCategory,
    private val category: Category
) : ViewModel(), Event<CategoryEvent> {

    private val _offerProductsState = MutableStateFlow(ProductsCategoryState())
    val offerProductsState = _offerProductsState.asStateFlow()

    private val _bestProductsState = MutableStateFlow(ProductsCategoryState())
    val bestProductsState = _bestProductsState.asStateFlow()

    init {
        fetchOfferProducts()
        fetchBestProducts()
    }

    override fun onEvent(event: CategoryEvent) {
        when (event) {
            CategoryEvent.BestProducts -> fetchBestProducts()
            CategoryEvent.OfferProducts -> fetchOfferProducts()
        }
    }

    private fun fetchOfferProducts() {
        viewModelScope.launch {
            _offerProductsState.value = ProductsCategoryState(null, true, null)
            when (val result = getOfferProductsByCategory(category.category)) {
                is Resource.Error -> {
                    _offerProductsState.value = ProductsCategoryState(null, false, result.message!!)
                }

                is Resource.Success -> {
                    _offerProductsState.value = ProductsCategoryState(result.data!!, false, null)
                }
            }
        }
    }

    private fun fetchBestProducts() {
        viewModelScope.launch {
            _bestProductsState.value = ProductsCategoryState(null, true, null)
            when (val result = getBestProductsByCategory(category.category)) {
                is Resource.Error -> {
                    _bestProductsState.value = ProductsCategoryState(null, false, result.message!!)
                }

                is Resource.Success -> {
                    _bestProductsState.value = ProductsCategoryState(
                        result.data!!, false, null
                    )
                }
            }
        }
    }
}