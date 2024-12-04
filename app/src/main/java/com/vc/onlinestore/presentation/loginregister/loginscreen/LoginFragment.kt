package com.vc.onlinestore.presentation.loginregister.loginscreen

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.vc.onlinestore.R
import com.vc.onlinestore.databinding.FragmentLoginBinding
import com.vc.onlinestore.dialog.setupBottomSheetDialog
import com.vc.onlinestore.presentation.shopping.ShoppingActivity
import com.vc.onlinestore.utils.collectLatestLifecycleFlow
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        login()
        forgotPassword()
        collectLoginState()
        collectResetPassword()
        navigate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun login() {
        binding.apply {
            buttonLoginLogin.setOnClickListener {
                val email = edEmailLogin.text.toString().trim()
                val password = edPasswordLogin.text.toString()
                viewModel.onEvent(event = LoginEvent.Login(email, password))
            }
        }
    }

    private fun forgotPassword() {
        binding.tvForgotPasswordLogin.setOnClickListener {
            setupBottomSheetDialog { email ->
                viewModel.onEvent(event = LoginEvent.ResetPassword(email))
            }
        }
    }

    private fun collectLoginState() {
        collectLatestLifecycleFlow(viewModel.state) { state ->
            when {
                state.isLoading -> {
                    binding.buttonLoginLogin.startAnimation()
                }

                state.user != null -> {
                    binding.buttonLoginLogin.revertAnimation()
                    Intent(requireContext(), ShoppingActivity::class.java).also { intent ->
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    }
                }

                state.errorMessage != null -> {
                    binding.buttonLoginLogin.revertAnimation()
                    Snackbar.make(binding.root, state.errorMessage, Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun collectResetPassword() {
        collectLatestLifecycleFlow(viewModel.resetPasswordState) { state ->
            when {
                state.isLoading -> {
                    //todo
                }

                state.errorMessage != null -> {
                    Snackbar.make(
                        binding.root,
                        "Error:${state.errorMessage}",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }

                state.email != null -> {
                    Snackbar.make(
                        binding.root,
                        "Reset link was sent to email: ${state.email}",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun navigate() {
        binding.tvDontHaveAnAccount.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }
}