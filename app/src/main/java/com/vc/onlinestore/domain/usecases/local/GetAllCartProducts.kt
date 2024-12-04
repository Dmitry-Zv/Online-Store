package com.vc.onlinestore.domain.usecases.local

import com.vc.onlinestore.domain.model.CartProduct
import com.vc.onlinestore.domain.repository.LocalRepository
import com.vc.onlinestore.utils.Resource
import javax.inject.Inject

class GetAllCartProducts @Inject constructor(
    private val repository: LocalRepository
) {
    suspend operator fun invoke(): Resource<List<CartProduct>> =
        repository.getAllCartProducts()
}