package com.vc.onlinestore.data.repository

import com.vc.onlinestore.data.network.ProductApi
import com.vc.onlinestore.domain.model.Product
import com.vc.onlinestore.domain.repository.ProductRepository
import com.vc.onlinestore.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val api: ProductApi
) : ProductRepository {

    override suspend fun getSpecialProducts(): Resource<List<Product>> =
        withContext(Dispatchers.IO) {
            try {
                val response = api.getSpecialProducts()
                if (response.isSuccessful) {
                    val products = checkNotNull(response.body())
                    Resource.Success(data = products)
                } else {
                    val errorBody = checkNotNull(response.errorBody())
                    Resource.Error(message = errorBody.string())
                }
            } catch (e: Exception) {
                Resource.Error(message = e.message ?: "Unknown message")
            }
        }

    override suspend fun getBestDealsProducts(): Resource<List<Product>> =
        withContext(Dispatchers.IO) {
            try {
                val response = api.getBestDealsProducts()
                if (response.isSuccessful) {
                    val products = checkNotNull(response.body())
                    Resource.Success(data = products)
                } else {
                    val errorBody = checkNotNull(response.errorBody())
                    Resource.Error(message = errorBody.string())
                }
            } catch (e: Exception) {
                Resource.Error(message = e.message ?: "Unknown error")
            }
        }

    override suspend fun getBestProducts(): Resource<List<Product>> =
        withContext(Dispatchers.IO) {
            try {
                val response = api.getBestProducts()
                if (response.isSuccessful) {
                    val products = checkNotNull(response.body())
                    Resource.Success(data = products)
                } else {
                    val errorBody = checkNotNull(response.errorBody())
                    Resource.Error(message = errorBody.string())
                }
            } catch (e: Exception) {
                Resource.Error(message = e.message ?: "Unknown error")
            }
        }

    override suspend fun getOfferProductsByCategory(category: String): Resource<List<Product>> =
        withContext(Dispatchers.IO) {
            try {
                val response = api.getOfferProductsByCategory(category = category)
                if (response.isSuccessful) {
                    val products = checkNotNull(response.body())
                    Resource.Success(data = products)
                } else {
                    val errorBody = checkNotNull(response.errorBody())
                    Resource.Error(message = errorBody.string())
                }
            } catch (e: Exception) {
                Resource.Error(message = e.message ?: "Unknown error")
            }
        }

    override suspend fun getBestProductsByCategory(category: String): Resource<List<Product>> =
        withContext(Dispatchers.IO) {
            try {
                val response = api.getBestProductsByCategory(category = category)
                if (response.isSuccessful) {
                    val products = checkNotNull(response.body())
                    Resource.Success(data = products)
                } else {
                    val errorBody = checkNotNull(response.errorBody())
                    Resource.Error(message = errorBody.string())
                }
            } catch (e: Exception) {
                Resource.Error(message = e.message ?: "Unknown error")
            }
        }

}