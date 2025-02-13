package com.vc.onlinestore.domain.repository

interface TokenRepository {

    fun saveToken(token: String)

    fun getToken(): String
}