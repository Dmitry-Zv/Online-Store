package com.vc.onlinestore.presentation.shopping.cartscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.vc.onlinestore.R
import com.vc.onlinestore.adapters.CartProductAdapter
import com.vc.onlinestore.databinding.FragmentCartBinding
import com.vc.onlinestore.domain.model.CartProduct
import com.vc.onlinestore.helper.VerticalItemDecoration
import com.vc.onlinestore.presentation.shopping.ShoppingShareViewModel
import com.vc.onlinestore.utils.collectLatestLifecycleFlow
import com.vc.onlinestore.utils.showBottomNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    private val cartAdapter by lazy { CartProductAdapter() }
    private val viewModel: CartViewModel by viewModels()
    private val sharedViewModel: ShoppingShareViewModel by activityViewModels()
    private var cartProducts: List<CartProduct>? = null
    private var totalPrice: Float = 0f

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCartRv()
        navigate()
        navigateUpListener()
        changeCartProduct()
        collectCartProductsState()
        collectPrice()
        deleteDialog()
    }

    override fun onResume() {
        super.onResume()
        showBottomNavigation()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupCartRv() {
        binding.apply {
            rvCart.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            rvCart.adapter = cartAdapter
            rvCart.addItemDecoration(VerticalItemDecoration())
        }
    }

    private fun navigate() {
        cartAdapter.onProductClick = { cartProduct ->
            val bundle = Bundle().apply {
                putParcelable("product", cartProduct.product)
            }
            findNavController().navigate(R.id.action_cartFragment_to_productDetailsFragment, bundle)
        }
        binding.buttonCheckout.setOnClickListener {
            if (cartProducts != null) {
                val bundle = Bundle().apply {
                    putFloat("totalPrice", totalPrice)
                    putParcelableArray("cartProduct", cartProducts!!.toTypedArray())
                    putBoolean("payment", true)
                }
                findNavController().navigate(R.id.action_cartFragment_to_billingFragment, bundle)
            } else {
                Snackbar.make(
                    binding.root,
                    "Cart is empty. Fill cart with some products",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun navigateUpListener() {
        findNavController().addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.cartFragment) {
                viewModel.getCartProducts()
            }
        }
    }

    private fun changeCartProduct() {
        cartAdapter.onPlusClick = { cartProduct ->
            viewModel.onEvent(event = CartEvent.IncreaseQuantity(cartProduct))
        }

        cartAdapter.onMinusClick = { cartProduct ->
            viewModel.onEvent(event = CartEvent.DecreaseQuantity(cartProduct))
        }
    }

    private fun collectCartProductsState() {
        collectLatestLifecycleFlow(viewModel.cartProductsState) { state ->
            when {
                state.isLoading -> {
                    binding.progressbarCart.visibility = View.VISIBLE
                }

                state.errorMessage != null -> {
                    binding.progressbarCart.visibility = View.GONE
                    Snackbar.make(
                        binding.root,
                        "Error: ${state.errorMessage}",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }

                state.product != null -> {
                    this.cartProducts = state.product
                    sharedViewModel.setCartProducts(state.product)
                    binding.progressbarCart.visibility = View.GONE
                    if (state.product.isEmpty()) {
                        showEmptyCart()
                        hideOtherView()
                    } else {
                        hideEmptyCart()
                        showOtherView()
                    }
                    cartAdapter.setCartProducts(state.product)
                }
            }
        }
    }

    private fun collectPrice() {
        collectLatestLifecycleFlow(viewModel.productPrice) { productPrice ->
            productPrice?.let {
                totalPrice = productPrice
                binding.tvTotalPrice.text = "BYN ${String.format("%.2f", totalPrice)}"
            }
        }
    }

    private fun deleteDialog() {
        collectLatestLifecycleFlow(viewModel.deleteDialog) { cartProduct ->
            val alertDialog = AlertDialog.Builder(requireContext()).apply {
                setTitle("Delete item from cart")
                setMessage("Do you want to delete this item from your cart?")
                setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                setPositiveButton("Yes") { dialog, _ ->
                    viewModel.onEvent(event = CartEvent.DeleteCartProduct(cartProduct))
                    dialog.dismiss()
                }
            }.create()
            alertDialog.show()
        }
    }

    private fun showEmptyCart() {
        binding.layoutCartEmpty.visibility = View.VISIBLE
    }

    private fun hideEmptyCart() {
        binding.layoutCartEmpty.visibility = View.GONE
    }

    private fun showOtherView() {
        binding.apply {
            rvCart.visibility = View.VISIBLE
            totalBoxContainer.visibility = View.VISIBLE
            buttonCheckout.visibility = View.VISIBLE
        }
    }

    private fun hideOtherView() {
        binding.apply {
            rvCart.visibility = View.GONE
            totalBoxContainer.visibility = View.GONE
            buttonCheckout.visibility = View.GONE
        }
    }
}