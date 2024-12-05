package com.vc.onlinestore.presentation.shopping.detailscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.vc.onlinestore.R
import com.vc.onlinestore.adapters.ColorsAdapter
import com.vc.onlinestore.adapters.SizesAdapter
import com.vc.onlinestore.adapters.ViewPager2Images
import com.vc.onlinestore.databinding.FragmentProductDetailsBinding
import com.vc.onlinestore.domain.model.CartProduct
import com.vc.onlinestore.domain.model.Product
import com.vc.onlinestore.presentation.shopping.ShoppingShareViewModel
import com.vc.onlinestore.utils.collectLatestLifecycleFlow
import com.vc.onlinestore.utils.hideBottomNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetailsFragment : Fragment() {

    private var _binding: FragmentProductDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewPagerAdapter by lazy {
        ViewPager2Images()
    }
    private val colorsAdapter by lazy {
        ColorsAdapter()
    }
    private val sizesAdapter by lazy {
        SizesAdapter()
    }
    private val args by navArgs<ProductDetailsFragmentArgs>()
    private lateinit var product: Product
    private val viewModel: ProductDetailViewModel by viewModels()
    private val sharedViewModel: ShoppingShareViewModel by activityViewModels()
    private var selectedColor: Int? = null
    private var selectedSize: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        product = args.product
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        hideBottomNavigation()
        _binding = FragmentProductDetailsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
        setupColorsAdapter()
        setupSizesAdapter()
        setupDetailView()
        collectCartProductsState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupViewPager() {
        binding.apply {
            viewPagerProductsImages.adapter = viewPagerAdapter
        }
    }

    private fun setupColorsAdapter() {
        binding.apply {
            rvColors.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            rvColors.adapter = colorsAdapter
        }
    }

    private fun setupSizesAdapter() {
        binding.apply {
            rvSize.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            rvSize.adapter = sizesAdapter
        }
    }

    private fun setupDetailView() {
        binding.apply {
            tvProductName.text = product.name
            tvProductPrice.text = "BYN ${product.price}"
            tvProductDescription.text = product.description
            imageClose.setOnClickListener {
                findNavController().navigateUp()
            }
            if (product.colors.isNullOrEmpty()) {
                tvProductColors.visibility = View.INVISIBLE
            } else {
                tvProductColors.visibility = View.VISIBLE
            }
            if (product.size.isNullOrEmpty()) {
                tvProductSize.visibility = View.INVISIBLE
            } else {
                tvProductSize.visibility = View.VISIBLE
            }
        }
        viewPagerAdapter.setImages(product.images)
        product.colors?.let {
            colorsAdapter.setColors(it)
        }
        product.size?.let {
            sizesAdapter.setSizes(it)
        }
        colorsAdapter.onItemClick = { color ->
            selectedColor = color
        }
        sizesAdapter.onItemClick = { size ->
            selectedSize = size
        }
        binding.buttonAddToCart.setOnClickListener {
            viewModel.onEvent(
                event = ProductDetailsEvent.AddToCart(
                    CartProduct(
                        product = product,
                        quantity = 1,
                        selectedColor = selectedColor,
                        selectedSize = selectedSize
                    )
                )
            )
        }
    }

    private fun collectCartProductsState() {
        collectLatestLifecycleFlow(viewModel.cartProductsCategoryState) { state ->
            when {
                state.isLoading -> {
                    binding.buttonAddToCart.startAnimation()
                }

                state.product != null -> {
                    sharedViewModel.setCartProducts(state.product)
                    binding.buttonAddToCart.revertAnimation()
                    binding.buttonAddToCart.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.black
                        )
                    )
                    Snackbar.make(binding.root, "Successfully added", Snackbar.LENGTH_SHORT).show()
                }

                state.errorMessage != null -> {
                    binding.buttonAddToCart.revertAnimation()
                    Snackbar.make(
                        binding.root,
                        "Error: ${state.errorMessage}",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}