package com.vc.onlinestore.presentation.shopping.profilescreen

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.vc.onlinestore.R
import com.vc.onlinestore.databinding.FragmentProfileBinding
import com.vc.onlinestore.presentation.loginregister.LoginRegisterActivity
import com.vc.onlinestore.utils.collectLatestLifecycleFlow
import com.vc.onlinestore.utils.showBottomNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigate()
        navigateUpListener()
        collectUserState()
        collectLogoutState()
    }

    override fun onResume() {
        super.onResume()
        showBottomNavigation()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        findNavController().removeOnDestinationChangedListener { _, _, _ -> }
        _binding = null
    }

    private fun navigate() {
        binding.constraintProfile.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_userAccountFragment)
        }

        binding.linearAllOrders.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_ordersFragment)
        }

        binding.linearTrackOrder.setOnClickListener {
            Snackbar.make(binding.root, "This feature is not available yet", Snackbar.LENGTH_SHORT)
                .show()
        }

        binding.linearBilling.setOnClickListener {
            val action =
                ProfileFragmentDirections.actionProfileFragmentToBillingFragment(
                    0f,
                    emptyArray(),
                    false
                )
            findNavController().navigate(action)
        }

        binding.linearLogOut.setOnClickListener {
            viewModel.onEvent(event = ProfileEvent.Logout)
        }

        binding.tvVersion.text = "Version 1.0"
    }

    private fun navigateUpListener() {
        findNavController().addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.profileFragment) {
                viewModel.getUser()
            }
        }
    }

    private fun collectUserState() {
        collectLatestLifecycleFlow(viewModel.userState) { userState ->
            when {
                userState.isLoading -> {
                    binding.progressbarSettings.visibility = View.VISIBLE
                }

                userState.message != null -> {
                    binding.progressbarSettings.visibility = View.GONE
                    showErrorMessage(userState.message)
                }

                userState.user != null -> {
                    val user = userState.user
                    binding.apply {
                        progressbarSettings.visibility = View.GONE
                        Glide.with(binding.root).load(user.imagePath)
                            .error(ColorDrawable(Color.BLACK)).into(imageUser)
                        tvUserName.text = "${user.firstName} ${user.lastName}"
                    }
                }
            }
        }
    }

    private fun collectLogoutState() {
        collectLatestLifecycleFlow(viewModel.logoutState) { message ->
            if (message == null) {
                Intent(requireContext(), LoginRegisterActivity::class.java).also {
                    startActivity(it)
                    requireActivity().finish()
                }
            } else {
                showErrorMessage(message)
            }
        }
    }

    private fun showErrorMessage(message: String) {
        Snackbar.make(binding.root, "Error: $message", Snackbar.LENGTH_SHORT).show()
    }
}