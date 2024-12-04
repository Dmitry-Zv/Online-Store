package com.vc.onlinestore.domain.repository

import com.vc.onlinestore.domain.model.Product
import com.vc.onlinestore.utils.Resource

interface ProductRepository {

    suspend fun getSpecialProducts(): Resource<List<Product>>

    suspend fun getBestDealsProducts(): Resource<List<Product>>

    suspend fun getBestProducts(): Resource<List<Product>>

    suspend fun getOfferProductsByCategory(category: String): Resource<List<Product>>

    suspend fun getBestProductsByCategory(category: String): Resource<List<Product>>
}