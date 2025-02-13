package com.vc.onlinestore.domain.usecases.network

import com.vc.onlinestore.domain.model.Product
import com.vc.onlinestore.domain.repository.ProductRepository
import com.vc.onlinestore.utils.Resource
import javax.inject.Inject

class GetOfferProductsByCategory @Inject constructor(
    private val repository: ProductRepository
) {

    suspend operator fun invoke(category:String, token:String) :Resource<List<Product>> =
        repository.getOfferProductsByCategory(category, token)
}