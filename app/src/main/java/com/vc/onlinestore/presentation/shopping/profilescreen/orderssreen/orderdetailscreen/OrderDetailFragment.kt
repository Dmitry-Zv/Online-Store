package com.vc.onlinestore.presentation.shopping.profilescreen.orderssreen.orderdetailscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vc.onlinestore.adapters.BillingProductsAdapter
import com.vc.onlinestore.data.firebase.dto.order.Order
import com.vc.onlinestore.data.firebase.dto.order.OrderStatus
import com.vc.onlinestore.data.firebase.dto.order.getOrderStatus
import com.vc.onlinestore.databinding.FragmentOrderDetailBinding
import com.vc.onlinestore.helper.VerticalItemDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderDetailFragment : Fragment() {
    private var _binding: FragmentOrderDetailBinding? = null
    private val binding get() = _binding!!

    private val billingProductsAdapter by lazy {
        BillingProductsAdapter()
    }
    private val args by navArgs<OrderDetailFragmentArgs>()
    private lateinit var order: Order

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        order = args.order
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupOrderRv()
        setupView()
        navigateBack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupOrderRv() {
        binding.apply {
            rvProducts.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            rvProducts.adapter = billingProductsAdapter
            rvProducts.addItemDecoration(VerticalItemDecoration())
        }
    }

    private fun setupView() {
        binding.apply {
            tvOrderId.text = "Order #${order.orderId}"

            stepView.setSteps(
                mutableListOf(
                    OrderStatus.Ordered.status,
                    OrderStatus.Confirmed.status,
                    OrderStatus.Shipped.status,
                    OrderStatus.Delivered.status,
                    OrderStatus.Ordered.status,
                    OrderStatus.Ordered.status,
                )
            )

            val currentOrderState = when (getOrderStatus(order.orderStatus)) {
                is OrderStatus.Ordered -> 0
                is OrderStatus.Confirmed -> 1
                is OrderStatus.Shipped -> 2
                is OrderStatus.Delivered -> 3
                else -> 0
            }

            stepView.go(currentOrderState, false)
            if (currentOrderState == 3) {
                stepView.done(true)
            }
            tvFullName.text = order.address.fullName
            tvAddress.text = "${order.address.street} ${order.address.city}"
            tvPhoneNumber.text = order.address.phone
            tvTotalPrice.text = "$ ${order.totalPrice}"
            billingProductsAdapter.setBillingProducts(order.products)
        }
    }

    private fun navigateBack(){
        binding.toolbarOrderDetails.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}