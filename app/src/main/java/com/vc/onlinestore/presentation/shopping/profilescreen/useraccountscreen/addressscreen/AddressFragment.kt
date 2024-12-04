package com.vc.onlinestore.presentation.shopping.profilescreen.useraccountscreen.addressscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.vc.onlinestore.data.firebase.dto.Address
import com.vc.onlinestore.databinding.FragmentAddressBinding
import com.vc.onlinestore.utils.collectLatestLifecycleFlow
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddressFragment : Fragment() {

    private var _binding: FragmentAddressBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddressViewModel by viewModels()
    private val args by navArgs<AddressFragmentArgs>()
    private var address: Address? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        address = args.address
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkAddress()
        saveAddress()
        deleteAddress()
        navigateBack()
        collectAddressState()
        collectDeleteAddressState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun checkAddress() {
        if (address == null) {
            binding.buttonDelelte.visibility = View.GONE
        } else {
            binding.apply {
                edAddressTitle.setText(address?.addressTitle)
                edFullName.setText(address?.fullName)
                edStreet.setText(address?.street)
                edPhone.setText(address?.phone)
                edCity.setText(address?.city)
                edState.setText(address?.state)
            }
        }
    }

    private fun saveAddress() {
        binding.apply {
            buttonSave.setOnClickListener {
                val address = Address(
                    addressTitle = edAddressTitle.text.trim().toString(),
                    fullName = edFullName.text.trim().toString(),
                    street = edStreet.text.trim().toString(),
                    phone = edPhone.text.trim().toString(),
                    city = edCity.text.trim().toString(),
                    state = edState.text.trim().toString()
                )

                viewModel.onEvent(event = AddressEvent.SaveAddress(address))
            }
        }
    }

    private fun deleteAddress() {
        binding.buttonDelelte.setOnClickListener {
            viewModel.onEvent(event = AddressEvent.DeleteAddress(address!!))
        }
    }

    private fun navigateBack() {
        binding.toolbarAddresses.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun collectAddressState() {
        collectLatestLifecycleFlow(viewModel.addressState) { addressState ->
            when {
                addressState.isLoading -> {
                    binding.progressbarAddress.visibility = View.VISIBLE
                }

                addressState.errorMessage != null -> {
                    binding.progressbarAddress.visibility = View.GONE
                    Snackbar.make(
                        binding.root,
                        "Error: ${addressState.errorMessage}",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }

                addressState.address != null -> {
                    binding.progressbarAddress.visibility = View.GONE
                    findNavController().navigateUp()
                }
            }
        }
        collectLatestLifecycleFlow(viewModel.error) { error ->
            Snackbar.make(
                binding.root,
                "Error: $error",
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    private fun collectDeleteAddressState() {
        collectLatestLifecycleFlow(viewModel.deleteAddressState) { deleteAddressState ->
            when {
                deleteAddressState.isLoading -> {
                    binding.progressbarAddress.visibility = View.VISIBLE
                }

                deleteAddressState.errorMessage != null -> {
                    binding.progressbarAddress.visibility = View.GONE
                    Snackbar.make(
                        binding.root,
                        "Error: ${deleteAddressState.errorMessage}",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }

                deleteAddressState.address != null -> {
                    binding.progressbarAddress.visibility = View.GONE
                    findNavController().navigateUp()
                }
            }
        }
    }
}