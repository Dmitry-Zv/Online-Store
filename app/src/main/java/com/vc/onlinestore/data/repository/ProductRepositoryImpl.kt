package com.vc.onlinestore.data.repository

import com.vc.onlinestore.data.firebase.dto.User
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

    override suspend fun getSpecialProducts(token: String): Resource<List<Product>> =
        withContext(Dispatchers.IO) {
            try {
                val response = api.getSpecialProducts(token)
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

    override suspend fun getBestDealsProducts(token: String): Resource<List<Product>> =
        withContext(Dispatchers.IO) {
            try {
                val response = api.getBestDealsProducts(token)
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

    override suspend fun getBestProducts(token: String): Resource<List<Product>> =
        withContext(Dispatchers.IO) {
            try {
                val response = api.getBestProducts(token)
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

    override suspend fun getOfferProductsByCategory(
        category: String,
        token: String
    ): Resource<List<Product>> =
        withContext(Dispatchers.IO) {
            try {
                val response = api.getOfferProductsByCategory(category = category, token = token)
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

    override suspend fun getBestProductsByCategory(
        category: String,
        token: String
    ): Resource<List<Product>> =
        withContext(Dispatchers.IO) {
            try {
                val response = api.getBestProductsByCategory(category = category, token = token)
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

    override suspend fun getProductsByName(name: String, token: String): Resource<List<Product>> =
        withContext(Dispatchers.IO) {
            try {
                val response = api.getProductsByName(name = name, token = token)
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

    override suspend fun authorize(user: User): Resource<String> =
        withContext(Dispatchers.IO) {
            try {
                val response = api.authorize(user = user)
                if (response.isSuccessful) {
                    val token = checkNotNull(response.headers()["Authorization"])
                    Resource.Success(data = token)
                } else {
                    val errorBody = checkNotNull(response.errorBody())
                    Resource.Error(message = errorBody.string())
                }
            } catch (e: Exception) {
                Resource.Error(message = e.message ?: "Unknown error")
            }
        }

}