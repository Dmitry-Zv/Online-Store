package com.vc.onlinestore.domain.usecases.network

import com.vc.onlinestore.domain.repository.TokenRepository
import javax.inject.Inject

class GetToken @Inject constructor(
    private val repository: TokenRepository
) {

    operator fun invoke(): String =
        repository.getToken()
}