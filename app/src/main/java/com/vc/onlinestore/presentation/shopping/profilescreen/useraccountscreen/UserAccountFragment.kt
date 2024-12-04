package com.vc.onlinestore.presentation.shopping.profilescreen.useraccountscreen

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.vc.onlinestore.data.firebase.dto.User
import com.vc.onlinestore.databinding.FragmentUserAccountBinding
import com.vc.onlinestore.dialog.setupBottomSheetDialog
import com.vc.onlinestore.utils.collectLatestLifecycleFlow
import com.vc.onlinestore.utils.hideBottomNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserAccountFragment : Fragment() {
    private var _binding: FragmentUserAccountBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UserAccountViewModel by viewModels()
    private val imageActivityResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            if (activityResult.resultCode == RESULT_OK && activityResult.data != null) {
                imageUri = activityResult.data?.data
                Glide.with(requireContext()).load(imageUri).
                override(300,300).into(binding.imageUser)
                Toast.makeText(requireContext(), "Success loading image", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Error with loading image", Toast.LENGTH_SHORT)
                    .show()
            }

        }

    private var imageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        hideBottomNavigation()
        _binding = FragmentUserAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateUser()
        forgotPassword()
        navigateBack()
        collectUserState()
        collectEditState()
        collectResetPassword()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateUser() {
        binding.apply {
            buttonSave.setOnClickListener {
                val user = User(
                    firstName = edFirstName.text.toString().trim(),
                    lastName = edLastName.text.toString().trim(),
                    email = edEmail.text.toString().trim()
                )
                viewModel.onEvent(event = UserAccountEvent.UpdateUser(user, imageUri))

            }
        }
        binding.imageUser.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            imageActivityResultLauncher.launch(
                Intent.createChooser(
                    intent,
                    "Select image from here..."
                )
            )
        }
    }

    private fun forgotPassword() {
        binding.tvUpdatePassword.setOnClickListener {
            setupBottomSheetDialog { email ->
                viewModel.onEvent(event = UserAccountEvent.ResetPassword(email))
            }
        }
    }

    private fun navigateBack() {
        binding.toolbarUserAccount.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun collectUserState() {
        collectLatestLifecycleFlow(viewModel.userState) { userState ->
            when {
                userState.isLoading -> {
                    showUserLoading()
                }

                userState.message != null -> {
                    hideUserLoading()
                    showError(userState.message)
                }

                userState.user != null -> {
                    hideUserLoading()
                    showUserInformation(userState.user)
                }
            }

        }
    }

    private fun collectEditState() {
        collectLatestLifecycleFlow(viewModel.editInfoState) { editInfoState ->
            when {
                editInfoState.isLoading -> {
                    binding.buttonSave.startAnimation()
                }

                editInfoState.message != null -> {
                    binding.buttonSave.revertAnimation()
                    showError(editInfoState.message)
                }

                editInfoState.user != null -> {
                    binding.buttonSave.revertAnimation()
                    findNavController().navigateUp()
                    Snackbar.make(
                        binding.root,
                        "User was successfully update",
                        Snackbar.LENGTH_SHORT
                    ).show()
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
                    showError(state.errorMessage)
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

    private fun showUserLoading() {
        binding.apply {
            progressbarAccount.visibility = View.VISIBLE
            imageUser.visibility = View.INVISIBLE
            imageEdit.visibility = View.INVISIBLE
            edFirstName.visibility = View.INVISIBLE
            edLastName.visibility = View.INVISIBLE
            edEmail.visibility = View.INVISIBLE
            tvUpdatePassword.visibility = View.INVISIBLE
            buttonSave.visibility = View.INVISIBLE
        }
    }

    private fun hideUserLoading() {
        binding.apply {
            progressbarAccount.visibility = View.INVISIBLE
            imageUser.visibility = View.VISIBLE
            imageEdit.visibility = View.VISIBLE
            edFirstName.visibility = View.VISIBLE
            edLastName.visibility = View.VISIBLE
            edEmail.visibility = View.VISIBLE
            tvUpdatePassword.visibility = View.VISIBLE
            buttonSave.visibility = View.VISIBLE
        }
    }

    private fun showError(message: String) {
        Snackbar.make(binding.root, "Error: $message", Snackbar.LENGTH_SHORT).show()
    }

    private fun showUserInformation(user: User) {
        binding.apply {
            Glide.with(this@UserAccountFragment).load(user.imagePath)
                .error(ColorDrawable(Color.BLACK)).into(imageUser)
            edFirstName.setText(user.firstName)
            edLastName.setText(user.lastName)
            edEmail.setText(user.email)
        }
    }
}