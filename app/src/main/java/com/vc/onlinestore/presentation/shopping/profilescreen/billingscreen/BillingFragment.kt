package com.vc.onlinestore.presentation.shopping.profilescreen.billingscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.vc.onlinestore.R
import com.vc.onlinestore.adapters.AddressAdapter
import com.vc.onlinestore.adapters.BillingProductsAdapter
import com.vc.onlinestore.data.firebase.dto.Address
import com.vc.onlinestore.data.firebase.dto.order.Order
import com.vc.onlinestore.data.firebase.dto.order.OrderStatus
import com.vc.onlinestore.databinding.FragmentBillingBinding
import com.vc.onlinestore.domain.model.CartProduct
import com.vc.onlinestore.helper.HorizontalItemDecoration
import com.vc.onlinestore.presentation.shopping.profilescreen.billingscreen.orderscreen.OrderEvent
import com.vc.onlinestore.presentation.shopping.profilescreen.billingscreen.orderscreen.OrderViewModel
import com.vc.onlinestore.utils.collectLatestLifecycleFlow
import com.vc.onlinestore.utils.hideBottomNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BillingFragment : Fragment() {

    private var _binding: FragmentBillingBinding? = null
    private val binding get() = _binding!!

    private val addressAdapter by lazy {
        AddressAdapter()
    }
    private val billingProductsAdapter by lazy {
        BillingProductsAdapter()
    }
    private val billingViewModel: BillingViewModel by viewModels()
    private val orderViewModel: OrderViewModel by viewModels()
    private val args by navArgs<BillingFragmentArgs>()
    private var totalPrice: Float = 0f
    private var cartProducts = emptyList<CartProduct>()
    private var selectedAddress: Address? = null
    private var payment: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        totalPrice = args.totalPrice
        cartProducts = args.cartProduct.toList()
        payment = args.payment
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        hideBottomNavigation()
        _binding = FragmentBillingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAddressRv()
        setupBillingProductsRv()
        if (payment) {
            showPayment()
        } else {
            hidePayment()
        }
        navigate()
        navigateBack()
        navigateUpListener()
        order()
        collectAddresses()
        collectOrderState()
        binding.tvTotalPrice.text = "BYN ${String.format("%.2f", totalPrice)}"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        findNavController().removeOnDestinationChangedListener { _, _, _ -> }
        _binding = null
    }

    private fun setupAddressRv() {
        binding.apply {
            rvAddress.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            rvAddress.adapter = addressAdapter
            rvAddress.addItemDecoration(HorizontalItemDecoration())
        }
        addressAdapter.onClick = { address: Address ->
            selectedAddress = address
            if (!payment) {
                val bundle = Bundle().apply {
                    putParcelable("address", selectedAddress)
                }
                findNavController().navigate(R.id.action_billingFragment_to_addressFragment, bundle)
            }
        }
    }

    private fun setupBillingProductsRv() {
        binding.apply {
            rvProducts.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            rvProducts.adapter = billingProductsAdapter
            rvProducts.addItemDecoration(HorizontalItemDecoration())
        }
        billingProductsAdapter.setBillingProducts(cartProducts)
    }

    private fun showPayment() {
        binding.apply {
            buttonPlaceOrder.visibility = View.VISIBLE
            totalBoxContainer.visibility = View.VISIBLE
            middleLine.visibility = View.VISIBLE
            bottomLine.visibility = View.VISIBLE
        }
    }

    private fun hidePayment() {
        binding.apply {
            buttonPlaceOrder.visibility = View.INVISIBLE
            totalBoxContainer.visibility = View.INVISIBLE
            middleLine.visibility = View.INVISIBLE
            bottomLine.visibility = View.INVISIBLE
        }
    }

    private fun navigate() {
        binding.imageAddAddress.setOnClickListener {
            findNavController().navigate(R.id.action_billingFragment_to_addressFragment)
        }
    }

    private fun navigateBack() {
        binding.toolbarBilling.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun navigateUpListener() {
        findNavController().addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.billingFragment) {
                billingViewModel.getAddressesForUser()
            }
        }
    }

    private fun order() {
        binding.buttonPlaceOrder.setOnClickListener {
            if (selectedAddress == null) {
                Snackbar.make(binding.root, "Please select an address", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            showOrderConfirmationDialog()
        }
    }

    private fun showOrderConfirmationDialog() {
        val alertDialog = AlertDialog.Builder(requireContext()).apply {
            setTitle("Order items")
            setMessage("Do you want to order your cart items?")
            setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            setPositiveButton("Yes") { dialog, _ ->
                val order = Order(
                    orderStatus = OrderStatus.Ordered.status,
                    totalPrice = totalPrice,
                    products = cartProducts,
                    address = selectedAddress!!
                )
                orderViewModel.onEvent(event = OrderEvent.PlaceOrder(order))
                dialog.dismiss()
            }
        }.create()
        alertDialog.show()
    }

    private fun collectAddresses() {
        collectLatestLifecycleFlow(billingViewModel.addressesState) { billingAddressesState ->
            when {
                billingAddressesState.isLoading -> {
                    binding.progressbarAddress.visibility = View.VISIBLE
                }

                billingAddressesState.errorMessage != null -> {
                    binding.progressbarAddress.visibility = View.GONE
                    showErrorMessage(billingAddressesState.errorMessage)
                }

                billingAddressesState.addresses != null -> {
                    binding.progressbarAddress.visibility = View.GONE
                    addressAdapter.setAddress(billingAddressesState.addresses)
                }
            }
        }
    }

    private fun collectOrderState() {
        collectLatestLifecycleFlow(orderViewModel.orderState) { orderState ->
            when {
                orderState.isLoading -> {
                    binding.buttonPlaceOrder.startAnimation()
                }

                orderState.errorMessage != null -> {
                    binding.buttonPlaceOrder.revertAnimation()
                    showErrorMessage(orderState.errorMessage)
                }

                orderState.order != null -> {
                    binding.buttonPlaceOrder.revertAnimation()
                    findNavController().navigateUp()
                    Snackbar.make(binding.root, "Your order was placed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun showErrorMessage(message: String) {
        Snackbar.make(
            binding.root,
            "Error: ${message}",
            Snackbar.LENGTH_SHORT
        ).show()
    }
}