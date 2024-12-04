package com.vc.onlinestore.presentation.loginregister.loginscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.vc.onlinestore.data.firebase.dto.User
import com.vc.onlinestore.domain.usecases.firebase.LogIn
import com.vc.onlinestore.domain.usecases.firebase.ResetPassword
import com.vc.onlinestore.domain.usecases.firebase.SaveUserInfo
import com.vc.onlinestore.domain.usecases.firebase.SignInWithGoogle
import com.vc.onlinestore.presentation.common.Event
import com.vc.onlinestore.presentation.common.ResetPasswordState
import com.vc.onlinestore.presentation.loginregister.registerscreen.RegisterState
import com.vc.onlinestore.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val login: LogIn,
    private val resetPassword: ResetPassword,
    private val signInWithGoogle: SignInWithGoogle,
    private val saveUserInfo: SaveUserInfo
) : ViewModel(), Event<LoginEvent> {

    private val _state = MutableSharedFlow<LoginState>()
    val state = _state.asSharedFlow()
    private val _resetPasswordState = MutableSharedFlow<ResetPasswordState>()
    val resetPasswordState = _resetPasswordState.asSharedFlow()
    private val _googleSignIn = MutableStateFlow(GoogleSignInState())
    val googleSignIn = _googleSignIn.asStateFlow()

    override fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.Login -> performLogin(email = event.email, password = event.password)
            is LoginEvent.ResetPassword -> performResetPasswordState(email = event.email)
            is LoginEvent.SignInWithGoogle -> googleSignIn(token = event.token)
        }
    }

    private fun performLogin(email: String, password: String) {
        viewModelScope.launch {
            _state.emit(LoginState(user = null, isLoading = true, null))
            when (val result = login(email = email, password = password)) {
                is Resource.Error -> {
                    _state.emit(LoginState(null, false, result.message!!))
                }

                is Resource.Success -> {
                    _state.emit(LoginState(result.data!!, false, null))
                }
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

    private fun googleSignIn(token: String) {
        viewModelScope.launch {
            _googleSignIn.value = GoogleSignInState(null, true, null)
            when (val result = signInWithGoogle(token)) {
                is Resource.Error -> {
                    _googleSignIn.value = GoogleSignInState(null, false, result.message!!)
                }

                is Resource.Success -> {
                    saveUser(result.data!!.uid, User.firebaseUserToUser(result.data), result.data)
                }
            }
        }
    }

    private suspend fun saveUser(uid: String, user: User, firebaseUser: FirebaseUser) {
        when (val result = saveUserInfo(uid, user)) {
            is Resource.Error -> _googleSignIn.value =
                GoogleSignInState(null, false, result.message!!)

            is Resource.Success -> _googleSignIn.value =
                GoogleSignInState(firebaseUser, false, null)
        }
    }
}