package com.vc.onlinestore.domain.repository

interface SharedPrefRepository {

    fun isIntroductionScreenWasShown(): Boolean

    fun startButtonClick()
}