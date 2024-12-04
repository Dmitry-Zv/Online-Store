package com.vc.onlinestore.presentation.shopping.homescreen.categories

import com.vc.onlinestore.domain.model.Product

data class ProductsCategoryState(
    val product: List<Product>? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)