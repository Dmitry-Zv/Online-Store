package com.vc.onlinestore.presentation.loginregister.registerscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.vc.onlinestore.data.firebase.dto.User
import com.vc.onlinestore.domain.usecases.firebase.CreateAccountWithEmailAndPassword
import com.vc.onlinestore.domain.usecases.firebase.SaveUserInfo
import com.vc.onlinestore.presentation.common.Event
import com.vc.onlinestore.utils.RegisterFieldState
import com.vc.onlinestore.utils.RegisterValidation
import com.vc.onlinestore.utils.Resource
import com.vc.onlinestore.utils.validateEmail
import com.vc.onlinestore.utils.validatePassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val createAccountWithEmailAndPassword: CreateAccountWithEmailAndPassword,
    private val saveUserInfoUseCase: SaveUserInfo
) : ViewModel(), Event<RegisterEvent> {

    private val _state = MutableStateFlow(RegisterState())
    val state = _state.asStateFlow()
    private val _validation = Channel<RegisterFieldState>()
    val validation = _validation.receiveAsFlow()

    override fun onEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.Register -> performRegister(
                user = event.user,
                password = event.password
            )
        }
    }

    private fun performRegister(user: User, password: String) {
        viewModelScope.launch {
            if (checkValidation(user.email, password)) {
                _state.value = RegisterState(null, true, null)
                when (val result =
                    createAccountWithEmailAndPassword(user = user, password = password)) {
                    is Resource.Error -> {
                        _state.value = RegisterState(null, false, result.message!!)
                    }

                    is Resource.Success -> {
                        saveUserInfo(result.data!!.uid, user, result.data)
                    }
                }
            } else {
                val registerFieldState = RegisterFieldState(
                    email = validateEmail(user.email),
                    password = validatePassword(password)
                )
                _validation.send(registerFieldState)
            }
        }
    }

    private suspend fun saveUserInfo(userUid: String, user: User, firebaseUser: FirebaseUser) {
        when (val result = saveUserInfoUseCase(userUid, user)) {
            is Resource.Error -> _state.value = RegisterState(null, false, result.message!!)
            is Resource.Success -> _state.value = RegisterState(
                firebaseUser, false, null
            )
        }
    }

    private fun checkValidation(email: String, password: String): Boolean {
        val emailValidation = validateEmail(email)
        val passwordValidation = validatePassword(password)
        return emailValidation is RegisterValidation.Success && passwordValidation is RegisterValidation.Success
    }

}