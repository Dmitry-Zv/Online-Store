package com.vc.onlinestore.domain.usecases.network

import com.vc.onlinestore.domain.model.Product
import com.vc.onlinestore.domain.repository.ProductRepository
import com.vc.onlinestore.utils.Resource
import javax.inject.Inject

class GetSpecialProducts @Inject constructor(
    private val repository: ProductRepository
) {

    suspend operator fun invoke(token: String): Resource<List<Product>> =
        repository.getSpecialProducts(token)
}