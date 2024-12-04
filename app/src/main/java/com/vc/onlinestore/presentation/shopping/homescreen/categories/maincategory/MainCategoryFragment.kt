package com.vc.onlinestore.presentation.shopping.homescreen.categories.maincategory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.vc.onlinestore.R
import com.vc.onlinestore.adapters.BestDealAdapter
import com.vc.onlinestore.adapters.BestProductAdapter
import com.vc.onlinestore.adapters.SpecialProductsAdapter
import com.vc.onlinestore.databinding.FragmentMainCategoryBinding
import com.vc.onlinestore.utils.collectLatestLifecycleFlow
import com.vc.onlinestore.utils.showBottomNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainCategoryFragment : Fragment() {
    private var _binding: FragmentMainCategoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var specialProductsAdapter: SpecialProductsAdapter
    private lateinit var bestDealAdapter: BestDealAdapter
    private lateinit var bestProductAdapter: BestProductAdapter
    private val viewModel: MainCategoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainCategoryBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpSpecialProductsRv()
        setUpBestDealAdapter()
        setUpBestProductAdapter()
        navigate()
        collectSpecialProductsState()
        collectBestDealsProductsState()
        collectBestProductsState()
    }

    override fun onResume() {
        super.onResume()
        showBottomNavigation()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpSpecialProductsRv() {
        specialProductsAdapter = SpecialProductsAdapter()
        binding.apply {
            rvSpecialProducts.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            rvSpecialProducts.adapter = specialProductsAdapter
        }
    }

    private fun setUpBestDealAdapter() {
        bestDealAdapter = BestDealAdapter()
        binding.apply {
            rvBestDealsProducts.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            rvBestDealsProducts.adapter = bestDealAdapter
        }
    }

    private fun setUpBestProductAdapter() {
        bestProductAdapter = BestProductAdapter()
        binding.apply {
            rvBestProducts.layoutManager =
                GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            rvBestProducts.adapter = bestProductAdapter
        }
    }

    private fun navigate() {
        specialProductsAdapter.onClick = { product ->
            val bundle = Bundle().apply {
                putParcelable("product", product)
            }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment, bundle)
        }
        bestDealAdapter.onClick = { product ->
            val bundle = Bundle().apply {
                putParcelable("product", product)
            }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment, bundle)
        }
        bestProductAdapter.onClick = { product ->
            val bundle = Bundle().apply {
                putParcelable("product", product)
            }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment, bundle)
        }
    }

    private fun collectSpecialProductsState() {
        collectLatestLifecycleFlow(viewModel.specialProductsState) { state ->
            when {
                state.isLoading -> {
                    binding.mainCategoryProgressBar.visibility = View.VISIBLE
                }

                state.errorMessage != null -> {
                    binding.mainCategoryProgressBar.visibility = View.GONE
                    Snackbar.make(
                        binding.root,
                        "Error: ${state.errorMessage}",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }

                state.product != null -> {
                    binding.mainCategoryProgressBar.visibility = View.GONE
                    specialProductsAdapter.setProducts(state.product)
                }
            }
        }
    }

    private fun collectBestDealsProductsState() {
        collectLatestLifecycleFlow(viewModel.bestDealsProductsState) { state ->
            when {
                state.isLoading -> {
                    binding.mainCategoryProgressBar.visibility = View.VISIBLE
                }

                state.errorMessage != null -> {
                    binding.mainCategoryProgressBar.visibility = View.GONE
                    Snackbar.make(
                        binding.root,
                        "Error: ${state.errorMessage}",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }

                state.product != null -> {
                    binding.mainCategoryProgressBar.visibility = View.GONE
                    bestDealAdapter.setProducts(state.product)
                }
            }
        }
    }

    private fun collectBestProductsState() {
        collectLatestLifecycleFlow(viewModel.bestProductsState) { state ->
            when {
                state.isLoading -> {
                    binding.mainCategoryProgressBar.visibility = View.VISIBLE
                }

                state.errorMessage != null -> {
                    binding.mainCategoryProgressBar.visibility = View.GONE
                    Snackbar.make(
                        binding.root,
                        "Error: ${state.errorMessage}",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }

                state.product != null -> {
                    binding.mainCategoryProgressBar.visibility = View.GONE
                    bestProductAdapter.setProducts(state.product)
                }
            }
        }
    }
}