package com.vc.onlinestore.data.repository

import android.content.SharedPreferences
import com.vc.onlinestore.domain.repository.SharedPrefRepository
import com.vc.onlinestore.utils.Constants.INTRODUCTION_KEY
import javax.inject.Inject

class SharedPrefRepositoryImpl @Inject constructor(
    private val pref: SharedPreferences
) : SharedPrefRepository {

    override fun isIntroductionScreenWasShown(): Boolean =
        pref.getBoolean(INTRODUCTION_KEY, false)

    override fun startButtonClick() {
        pref.edit().putBoolean(INTRODUCTION_KEY, true).apply()
    }
}