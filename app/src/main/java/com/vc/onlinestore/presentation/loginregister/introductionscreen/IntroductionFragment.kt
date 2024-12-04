package com.vc.onlinestore.presentation.loginregister.introductionscreen

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.vc.onlinestore.R
import com.vc.onlinestore.databinding.FragmentIntroductionBinding
import com.vc.onlinestore.presentation.loginregister.introductionscreen.IntroductionViewModel.Companion.ACCOUNT_OPTION
import com.vc.onlinestore.presentation.loginregister.introductionscreen.IntroductionViewModel.Companion.SHOPPING_ACTIVITY
import com.vc.onlinestore.presentation.shopping.ShoppingActivity
import com.vc.onlinestore.utils.collectLatestLifecycleFlow
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IntroductionFragment : Fragment() {

    private var _binding: FragmentIntroductionBinding? = null
    private val binding get() = _binding!!

    private val viewModel: IntroductionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIntroductionBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigate()
        collectNavigate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun navigate() {
        viewModel.startButtonClicked()
        binding.startButton.setOnClickListener {
            findNavController().navigate(R.id.action_introductionFragment_to_accountOptionsScreen)
        }
    }

    private fun collectNavigate() {
        collectLatestLifecycleFlow(viewModel.navigate) { navigate ->
            when (navigate) {
                SHOPPING_ACTIVITY -> {
                    Intent(requireContext(), ShoppingActivity::class.java).also { intent ->
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    }
                }

                ACCOUNT_OPTION -> {
                    findNavController().navigate(navigate)
                }
                else-> Unit
            }
        }
    }
}