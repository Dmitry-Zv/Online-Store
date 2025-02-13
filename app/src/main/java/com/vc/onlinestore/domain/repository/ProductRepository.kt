package com.vc.onlinestore.domain.repository

import com.vc.onlinestore.data.firebase.dto.User
import com.vc.onlinestore.domain.model.Product
import com.vc.onlinestore.utils.Resource

interface ProductRepository {

    suspend fun getSpecialProducts(token: String): Resource<List<Product>>

    suspend fun getBestDealsProducts(token: String): Resource<List<Product>>

    suspend fun getBestProducts(token: String): Resource<List<Product>>

    suspend fun getOfferProductsByCategory(category: String, token: String): Resource<List<Product>>

    suspend fun getBestProductsByCategory(category: String, token: String): Resource<List<Product>>

    suspend fun getProductsByName(name: String, token: String): Resource<List<Product>>

    suspend fun authorize(user: User): Resource<String>
}