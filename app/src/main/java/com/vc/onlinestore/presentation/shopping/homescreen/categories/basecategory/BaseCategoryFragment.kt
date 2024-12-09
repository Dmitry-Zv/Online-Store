package com.vc.onlinestore.presentation.shopping.homescreen.categories.basecategory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.vc.onlinestore.R
import com.vc.onlinestore.adapters.BestProductAdapter
import com.vc.onlinestore.databinding.FragmentBaseCategoryBinding
import com.vc.onlinestore.domain.model.Product
import com.vc.onlinestore.helper.HorizontalItemDecoration
import com.vc.onlinestore.helper.VerticalItemDecoration
import com.vc.onlinestore.utils.showBottomNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class BaseCategoryFragment : Fragment() {
    private var _binding: FragmentBaseCategoryBinding? = null
    private val binding get() = _binding!!
    private val offerAdapter: BestProductAdapter by lazy {
        BestProductAdapter()
    }
    private val bestProductsAdapter: BestProductAdapter by lazy {
        BestProductAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBaseCategoryBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpOfferAdapter()
        setUpBestProductsAdapter()
        navigate()
    }

    override fun onResume() {
        super.onResume()
        showBottomNavigation()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpOfferAdapter() {
        binding.apply {
            rvOffer.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            rvOffer.adapter = offerAdapter
            rvOffer.addItemDecoration(HorizontalItemDecoration())
        }
    }

    private fun setUpBestProductsAdapter() {
        binding.apply {
            rvBestProducts.layoutManager =
                GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            rvBestProducts.adapter = bestProductsAdapter
            rvBestProducts.addItemDecoration(VerticalItemDecoration())
        }
    }

    private fun navigate() {
        bestProductsAdapter.onClick = { product ->
            val bundle = Bundle().apply {
                putParcelable("product", product)
            }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment, bundle)
        }
        offerAdapter.onClick = { product ->
            val bundle = Bundle().apply {
                putParcelable("product", product)
            }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment, bundle)
        }
    }

    protected fun setOfferProducts(products: List<Product>) {
        offerAdapter.setProducts(products)
    }

    protected fun setBestProducts(products: List<Product>) {
        bestProductsAdapter.setProducts(products)
    }

    protected fun showErrorMessage(errorMessage: String) {
        Toast.makeText(requireContext(), "Error: $errorMessage", Toast.LENGTH_SHORT).show()
    }

    protected fun showOfferProgressBar() {
        binding.baseCategoryOfferProgressBar.visibility = View.VISIBLE
    }

    protected fun hideOfferProgressBar() {
        binding.baseCategoryOfferProgressBar.visibility = View.GONE
    }

    protected fun showBestProductsProgressBar() {
        binding.baseCategoryBestProductProgressBar.visibility = View.VISIBLE
    }

    protected fun hideBestProductsProgressBar() {
        binding.baseCategoryBestProductProgressBar.visibility = View.GONE
    }
}