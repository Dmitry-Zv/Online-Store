package com.vc.onlinestore.domain.usecases.network

import com.vc.onlinestore.data.firebase.dto.User
import com.vc.onlinestore.domain.repository.ProductRepository
import com.vc.onlinestore.utils.Resource
import javax.inject.Inject

class Authorize @Inject constructor(
    private val repository: ProductRepository
) {

    suspend operator fun invoke(user: User): Resource<String> =
        repository.authorize(user = user)
}