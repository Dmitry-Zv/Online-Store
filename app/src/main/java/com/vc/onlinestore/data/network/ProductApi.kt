package com.vc.onlinestore.data.network

import com.vc.onlinestore.domain.model.Product
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductApi {

    @GET("products/special-products")
    suspend fun getSpecialProducts(): Response<List<Product>>

    @GET("products/best-deals-products")
    suspend fun getBestDealsProducts(): Response<List<Product>>

    @GET("products/best-products")
    suspend fun getBestProducts(): Response<List<Product>>

    @GET("products/offer-products/{category}")
    suspend fun getOfferProductsByCategory(@Path("category") category: String): Response<List<Product>>

    @GET("products/best-products/{category}")
    suspend fun getBestProductsByCategory(@Path("category") category: String): Response<List<Product>>
}