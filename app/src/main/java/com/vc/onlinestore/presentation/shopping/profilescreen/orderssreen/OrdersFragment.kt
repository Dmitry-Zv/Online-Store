package com.vc.onlinestore.presentation.shopping.profilescreen.orderssreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.vc.onlinestore.adapters.OrdersAdapter
import com.vc.onlinestore.databinding.FragmentOrdersBinding
import com.vc.onlinestore.utils.collectLatestLifecycleFlow
import com.vc.onlinestore.utils.hideBottomNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrdersFragment : Fragment() {

    private var _binding: FragmentOrdersBinding? = null
    private val binding get() = _binding!!

    private val viewModel: OrdersViewModel by viewModels()
    private val ordersAdapter by lazy {
        OrdersAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        hideBottomNavigation()
        _binding = FragmentOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOrdersRv()
        navigateBack()
        collectAllOrders()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllOrders()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupOrdersRv() {
        binding.apply {
            rvAllOrders.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            rvAllOrders.adapter = ordersAdapter
        }
        ordersAdapter.onClick = { order ->
            val action = OrdersFragmentDirections.actionOrdersFragmentToOrderDetailFragment(order)
            findNavController().navigate(action)
        }
    }

    private fun navigateBack() {
        binding.toolbarAllOrders.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun collectAllOrders() {
        collectLatestLifecycleFlow(viewModel.ordersState) { ordersState ->
            when {
                ordersState.isLoading -> {
                    binding.progressbarAllOrders.visibility = View.VISIBLE
                }

                ordersState.message != null -> {
                    binding.progressbarAllOrders.visibility = View.GONE
                    showError(ordersState.message)
                }

                ordersState.orders != null -> {
                    binding.progressbarAllOrders.visibility = View.GONE
                    if (ordersState.orders.isEmpty()) {
                        binding.tvEmptyOrders.visibility = View.VISIBLE
                    } else {
                        binding.tvEmptyOrders.visibility = View.GONE
                    }
                    ordersAdapter.setOrders(ordersState.orders)
                }
            }
        }
    }

    private fun showError(message: String) {
        Snackbar.make(binding.root, "Error: $message", Snackbar.LENGTH_SHORT).show()
    }
}