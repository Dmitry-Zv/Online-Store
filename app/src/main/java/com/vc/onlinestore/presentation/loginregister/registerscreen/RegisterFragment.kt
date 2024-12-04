package com.vc.onlinestore.presentation.loginregister.registerscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.vc.onlinestore.R
import com.vc.onlinestore.data.firebase.dto.User
import com.vc.onlinestore.databinding.FragmentRegisterBinding
import com.vc.onlinestore.utils.RegisterValidation
import com.vc.onlinestore.utils.collectLatestLifecycleFlow
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        register()
        collectRegisterState()
        collectRegisterValidation()
        navigate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun register() {
        binding.apply {
            buttonRegisterRegister.setOnClickListener {
                val user = User(
                    firstName = edFirstNameRegister.text.toString().trim(),
                    lastName = edLastNameRegister.text.toString().trim(),
                    email = edEmailRegister.text.toString().trim()
                )
                viewModel.onEvent(
                    event = RegisterEvent.Register(
                        user = user,
                        password = edPasswordRegister.text.toString()
                    )
                )
            }
        }
    }

    private fun collectRegisterState() {
        collectLatestLifecycleFlow(viewModel.state) { state ->
            when {
                state.isLoading -> {
                    binding.buttonRegisterRegister.startAnimation()
                }

                state.firebaseUser != null -> {
                    binding.buttonRegisterRegister.revertAnimation()
                    Snackbar.make(
                        binding.root,
                        "You have created a new account, login now",
                        Snackbar.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                }

                state.errorMessage != null -> {
                    binding.buttonRegisterRegister.revertAnimation()
                    Snackbar.make(binding.root, state.errorMessage, Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun collectRegisterValidation() {
        collectLatestLifecycleFlow(viewModel.validation) { validation ->

            if (validation.email is RegisterValidation.Failed) {
                binding.edEmailRegister.apply {
                    requestFocus()
                    error = validation.email.message
                }
            }

            if (validation.password is RegisterValidation.Failed) {
                binding.edPasswordRegister.apply {
                    requestFocus()
                    error = validation.password.message
                }
            }

        }
    }

    private fun navigate() {
        binding.tvDoYouHaveAnAccount.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }
}