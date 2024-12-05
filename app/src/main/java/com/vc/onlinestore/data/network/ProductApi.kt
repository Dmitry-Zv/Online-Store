package com.vc.onlinestore.data.network

import com.vc.onlinestore.domain.model.Product
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductApi {

    @GET("products/best-discount-products")
    suspend fun getSpecialProducts(): Response<List<Product>>

    @GET("products/discount-products")
    suspend fun getBestDealsProducts(): Response<List<Product>>

    @GET("products/list")
    suspend fun getBestProducts(): Response<List<Product>>

    @GET("products/discount-products/{category}")
    suspend fun getOfferProductsByCategory(@Path("category") category: String): Response<List<Product>>

    @GET("products/{category}")
    suspend fun getBestProductsByCategory(@Path("category") category: String): Response<List<Product>>
}