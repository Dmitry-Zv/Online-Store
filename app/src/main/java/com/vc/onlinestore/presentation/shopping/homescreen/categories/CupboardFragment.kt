package com.vc.onlinestore.presentation.shopping.homescreen.categories

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.vc.onlinestore.domain.model.Category
import com.vc.onlinestore.domain.usecases.network.GetBestProductsByCategory
import com.vc.onlinestore.domain.usecases.network.GetOfferProductsByCategory
import com.vc.onlinestore.presentation.shopping.homescreen.categories.basecategory.BaseCategoryFragment
import com.vc.onlinestore.presentation.shopping.homescreen.categories.basecategory.BaseCategoryViewModelFactory
import com.vc.onlinestore.utils.collectLatestLifecycleFlow
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class CupboardFragment : BaseCategoryFragment() {
    @Inject
    lateinit var getOfferProductsByCategory: GetOfferProductsByCategory

    @Inject
    lateinit var getBestProductsByCategory: GetBestProductsByCategory

    private val viewModel by viewModels<CategoryViewModel> {
        BaseCategoryViewModelFactory(
            getOfferProductsByCategory,
            getBestProductsByCategory,
            Category.Cupboard
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectOfferProductsByCategory()
        collectBestProductsByCategory()
    }

    private fun collectOfferProductsByCategory() {
        collectLatestLifecycleFlow(viewModel.offerProductsState) { state ->
            when {
                state.isLoading -> {
                    showOfferProgressBar()
                }

                state.product != null -> {
                    hideOfferProgressBar()
                    setOfferProducts(state.product)
                }

                state.errorMessage != null -> {
                    hideOfferProgressBar()
                    showErrorMessage(state.errorMessage)
                }
            }
        }
    }

    private fun collectBestProductsByCategory() {
        collectLatestLifecycleFlow(viewModel.bestProductsState) { state ->
            when {
                state.isLoading -> {
                    showBestProductsProgressBar()
                }

                state.product != null -> {
                    hideBestProductsProgressBar()
                    setBestProducts(state.product)
                }

                state.errorMessage != null -> {
                    hideBestProductsProgressBar()
                    showErrorMessage(state.errorMessage)
                }
            }
        }
    }
}