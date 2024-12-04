package com.vc.onlinestore.presentation.loginregister.introductionscreen

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.vc.onlinestore.R
import com.vc.onlinestore.domain.usecases.sharedpref.IsIntroductionScreenWasShown
import com.vc.onlinestore.domain.usecases.sharedpref.StartButtonClick
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class IntroductionViewModel @Inject constructor(
    isIntroductionScreenWasShown: IsIntroductionScreenWasShown,
    private val startButtonClick: StartButtonClick,
    firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _navigate = MutableStateFlow(DEFAULT)
    val navigate = _navigate.asStateFlow()

    companion object {
        const val DEFAULT = 0
        val ACCOUNT_OPTION = R.id.action_introductionFragment_to_accountOptionsScreen
        const val SHOPPING_ACTIVITY = 2

    }

    init {
        val isButtonClicked = isIntroductionScreenWasShown()
        val user = firebaseAuth.currentUser

        if (user != null) {
            _navigate.value = SHOPPING_ACTIVITY
        } else if (isButtonClicked) {
            _navigate.value = ACCOUNT_OPTION
        } else {
            Unit
        }
    }

    fun startButtonClicked() {
        startButtonClick()
    }
}