package com.vc.onlinestore.presentation.shopping.homescreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.vc.onlinestore.R
import com.vc.onlinestore.adapters.BestProductAdapter
import com.vc.onlinestore.adapters.HomeViewPagerAdapter
import com.vc.onlinestore.databinding.FragmentHomeBinding
import com.vc.onlinestore.helper.VerticalItemDecoration
import com.vc.onlinestore.presentation.shopping.homescreen.categories.AccessoryFragment
import com.vc.onlinestore.presentation.shopping.homescreen.categories.ChairFragment
import com.vc.onlinestore.presentation.shopping.homescreen.categories.CupboardFragment
import com.vc.onlinestore.presentation.shopping.homescreen.categories.InstrumentFragment
import com.vc.onlinestore.presentation.shopping.homescreen.categories.MaterialFragment
import com.vc.onlinestore.presentation.shopping.homescreen.categories.TableFragment
import com.vc.onlinestore.presentation.shopping.homescreen.categories.maincategory.MainCategoryFragment
import com.vc.onlinestore.utils.collectLatestLifecycleFlow
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val productsAdapter: BestProductAdapter by lazy {
        BestProductAdapter()
    }
    val viewModel by viewModels<HomeViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViewPager()
        setUpProductsAdapter()
        search()
        collectState()
    }


    private fun setUpProductsAdapter() {
        binding.apply {
            rvProductsByName.layoutManager =
                GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            rvProductsByName.adapter = productsAdapter
            rvProductsByName.addItemDecoration(VerticalItemDecoration())
            productsAdapter.onClick = { product ->
                val bundle = Bundle().apply {
                    putParcelable("product", product)
                }
                findNavController().navigate(
                    R.id.action_homeFragment_to_productDetailsFragment,
                    bundle
                )
                searchView.onActionViewCollapsed()
            }
            productsAdapter.setProducts(listOf())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun collectState() {
        collectLatestLifecycleFlow(viewModel.state) { state ->
            when {
                state.isLoading -> {
                    binding.progressBar.visibility = View.VISIBLE

                }

                state.errorMessage != null -> {
                    binding.progressBar.visibility = View.GONE
                    Snackbar.make(binding.root, state.errorMessage, Snackbar.LENGTH_SHORT).show()
                }

                state.product != null -> {
                    binding.progressBar.visibility = View.GONE
                    productsAdapter.setProducts(products = state.product)
                }
            }
        }
    }

    private fun search() {
        binding.searchView.apply {
            setOnSearchClickListener {
                binding.rvProductsByName.visibility = View.VISIBLE
                binding.searchTv.visibility = View.GONE
                binding.tabLayout.visibility = View.GONE
                binding.viewpagerHome.visibility = View.GONE
            }
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    newText?.let {
                        if (it.isNotBlank()) viewModel.search(it)
                    }
                    return false
                }

            })

            setOnCloseListener {
                binding.rvProductsByName.visibility = View.GONE
                binding.tabLayout.visibility = View.VISIBLE
                binding.searchTv.visibility = View.VISIBLE
                binding.viewpagerHome.visibility = View.VISIBLE
                false
            }
        }
    }

    private fun initializeViewPager() {
        val categoriesFragments = arrayListOf(
            MainCategoryFragment(),
            ChairFragment(),
            CupboardFragment(),
            TableFragment(),
            AccessoryFragment(),
            InstrumentFragment(),
            MaterialFragment()
        )
        binding.viewpagerHome.isUserInputEnabled = false
        val viewPager2Adapter =
            HomeViewPagerAdapter(categoriesFragments, childFragmentManager, lifecycle)
        binding.viewpagerHome.adapter = viewPager2Adapter
        TabLayoutMediator(binding.tabLayout, binding.viewpagerHome) { tab, position ->
            when (position) {
                0 -> tab.text = "Main"
                1 -> tab.text = "Chair"
                2 -> tab.text = "Cupboard"
                3 -> tab.text = "Table"
                4 -> tab.text = "Accessory"
                5 -> tab.text = "Instrument"
                6 -> tab.text = "Material"
            }
        }.attach()
    }

}