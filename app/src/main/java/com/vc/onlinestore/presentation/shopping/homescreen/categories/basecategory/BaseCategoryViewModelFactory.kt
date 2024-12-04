package com.vc.onlinestore.presentation.shopping.homescreen.categories.basecategory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vc.onlinestore.domain.model.Category
import com.vc.onlinestore.domain.usecases.network.GetBestProductsByCategory
import com.vc.onlinestore.domain.usecases.network.GetOfferProductsByCategory
import com.vc.onlinestore.presentation.shopping.homescreen.categories.CategoryViewModel

class BaseCategoryViewModelFactory(
    private val getOfferProductsByCategory: GetOfferProductsByCategory,
    private val getBestProductsByCategory: GetBestProductsByCategory,
    private val category: Category
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CategoryViewModel(
                getOfferProductsByCategory,
                getBestProductsByCategory,
                category
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}