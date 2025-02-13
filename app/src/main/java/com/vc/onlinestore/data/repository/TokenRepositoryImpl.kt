package com.vc.onlinestore.data.repository

import android.content.SharedPreferences
import com.vc.onlinestore.domain.repository.TokenRepository
import javax.inject.Inject

class TokenRepositoryImpl @Inject constructor(
    private val preferences: SharedPreferences
) :TokenRepository{

    companion object{
        private const val TOKEN = "token"
    }
    override fun saveToken(token: String) =
        preferences.edit().putString(TOKEN, token).apply()

    override fun getToken(): String =
        preferences.getString(TOKEN, "")?:""
}