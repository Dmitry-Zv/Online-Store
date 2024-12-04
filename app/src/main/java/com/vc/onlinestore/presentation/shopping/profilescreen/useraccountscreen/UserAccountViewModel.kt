package com.vc.onlinestore.presentation.shopping.profilescreen.useraccountscreen

import android.app.Application
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.vc.onlinestore.OnlineStoreApp
import com.vc.onlinestore.data.firebase.dto.User
import com.vc.onlinestore.domain.usecases.firebase.GetUserInfo
import com.vc.onlinestore.domain.usecases.firebase.ResetPassword
import com.vc.onlinestore.domain.usecases.firebase.UpdateUserInfo
import com.vc.onlinestore.presentation.common.Event
import com.vc.onlinestore.presentation.common.ResetPasswordState
import com.vc.onlinestore.presentation.shopping.profilescreen.UserState
import com.vc.onlinestore.utils.RegisterValidation
import com.vc.onlinestore.utils.Resource
import com.vc.onlinestore.utils.validateEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserAccountViewModel @Inject constructor(
    private val getUserInfo: GetUserInfo,
    private val updateUserInfo: UpdateUserInfo,
    private val resetPassword: ResetPassword,
    app: Application
) : AndroidViewModel(app), Event<UserAccountEvent> {

    private val _userState = MutableStateFlow(UserState())
    val userState = _userState.asStateFlow()

    private val _editInfoState = MutableStateFlow(UserState())
    val editInfoState = _editInfoState.asStateFlow()

    private val _resetPasswordState = MutableSharedFlow<ResetPasswordState>()
    val resetPasswordState = _resetPasswordState.asSharedFlow()

    init {
        getUser()
    }

    override fun onEvent(event: UserAccountEvent) {
        when (event) {
            is UserAccountEvent.UpdateUser -> updateUser(
                user = event.user,
                imageUri = event.imageUri
            )

            is UserAccountEvent.ResetPassword -> performResetPasswordState(email = event.email)
        }
    }

    private fun getUser() {
        viewModelScope.launch {
            _userState.value = UserState(null, true, null)
            when (val result = getUserInfo()) {
                is Resource.Error -> {
                    _userState.value = UserState(null, false, result.message!!)
                }

                is Resource.Success -> {
                    _userState.value = UserState(result.data!!, false, null)
                }
            }
        }
    }

    private fun updateUser(user: User, imageUri: Uri?) {
        viewModelScope.launch {
            val areInputsValid = validateEmail(user.email) is RegisterValidation.Success &&
                    user.firstName.isNotBlank() &&
                    user.lastName.isNotBlank()

            if (!areInputsValid) {
                _editInfoState.value = UserState(null, false, "Check your inputs")
                return@launch
            }
            _editInfoState.value = UserState(null, true, null)
            if (imageUri == null) {
                saveUserInformation(user, true)
            } else {
                saveUserInformation(user, false, imageUri)
            }
        }

    }

    private suspend fun saveUserInformation(
        user: User,
        shouldRetrieveOldImage: Boolean,
        imageUri: Uri? = null
    ) {
        val imageBitmap = if (imageUri != null) {
            MediaStore.Images.Media.getBitmap(
                getApplication<OnlineStoreApp>().contentResolver,
                imageUri
            )
        } else {
            null
        }
        when (val result = updateUserInfo(
            user,
            shouldRetrieveOldImage,
            imageBitmap
        )) {
            is Resource.Error -> {
                _editInfoState.value = UserState(null, false, result.message!!)
            }

            is Resource.Success -> {
                _editInfoState.value = UserState(result.data, false, null)
            }
        }
    }

    private fun performResetPasswordState(email: String) {
        viewModelScope.launch {
            _resetPasswordState.emit(ResetPasswordState(null, true, null))
            when (val result = resetPassword(email)) {
                is Resource.Error -> {
                    _resetPasswordState.emit(ResetPasswordState(null, false, result.message))
                }

                is Resource.Success -> {
                    _resetPasswordState.emit(ResetPasswordState(result.data, false, null))
                }
            }
        }
    }

}