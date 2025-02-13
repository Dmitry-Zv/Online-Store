package com.vc.onlinestore.data.network

import com.vc.onlinestore.data.firebase.dto.User
import com.vc.onlinestore.domain.model.Product
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductApi {

    @GET("products/best-discount-products")
    suspend fun getSpecialProducts(
        @Header("Authorization") token: String
    ): Response<List<Product>>

    @GET("products/discount-products")
    suspend fun getBestDealsProducts(
        @Header("Authorization") token: String
    ): Response<List<Product>>

    @GET("products/list")
    suspend fun getBestProducts(
        @Header("Authorization") token: String
    ): Response<List<Product>>

    @GET("products/discount-products/{category}")
    suspend fun getOfferProductsByCategory(
        @Path("category") category: String,
        @Header("Authorization") token: String
    ): Response<List<Product>>

    @GET("products/{category}")
    suspend fun getBestProductsByCategory(
        @Path("category") category: String,
        @Header("Authorization") token: String
    ): Response<List<Product>>

    @GET("products")
    suspend fun getProductsByName(
        @Query("name") name: String,
        @Header("Authorization") token: String
    ): Response<List<Product>>

    @POST("api/v1/auth")
    suspend fun authorize(
        @Body user: User
    ): Response<Unit>
}