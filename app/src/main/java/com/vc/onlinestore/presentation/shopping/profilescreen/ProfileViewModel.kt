package com.vc.onlinestore.presentation.shopping.profilescreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vc.onlinestore.domain.usecases.firebase.GetUserInfo
import com.vc.onlinestore.domain.usecases.firebase.Logout
import com.vc.onlinestore.presentation.common.Event
import com.vc.onlinestore.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserInfo: GetUserInfo,
    private val logout: Logout
) : ViewModel(), Event<ProfileEvent> {

    private val _userState = MutableStateFlow(UserState())
    val userState = _userState.asStateFlow()
    private val _logoutState = MutableSharedFlow<String?>()
    val logoutState = _logoutState.asSharedFlow()

    init {
        getUser()
    }


    override fun onEvent(event: ProfileEvent) {
        when (event) {
            ProfileEvent.Logout -> performLogout()
        }
    }

    fun getUser() {
        _userState.value = UserState(null, true, null)
        viewModelScope.launch {
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

    private fun performLogout() {
        viewModelScope.launch {
            when (val result = logout()) {
                is Resource.Error -> {
                    _logoutState.emit(result.message!!)
                }

                is Resource.Success -> {
                    _logoutState.emit(null)
                }
            }
        }
    }
}