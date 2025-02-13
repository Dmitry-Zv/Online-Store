package com.vc.onlinestore.domain.usecases.network

import com.vc.onlinestore.domain.repository.TokenRepository
import javax.inject.Inject

class SaveToken @Inject constructor(
    private val repository: TokenRepository
) {

    operator fun invoke(token:String){
        repository.saveToken(token = token)
    }
}