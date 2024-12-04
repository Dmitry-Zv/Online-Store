package com.vc.onlinestore.data.repository

import com.vc.onlinestore.data.local.CartProductDao
import com.vc.onlinestore.domain.model.CartProduct
import com.vc.onlinestore.domain.repository.LocalRepository
import com.vc.onlinestore.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocalRepositoryImpl @Inject constructor(
    private val dao: CartProductDao
) : LocalRepository {

    override suspend fun insertCartProduct(cartProduct: CartProduct) {
        withContext(Dispatchers.IO) {
            dao.insertCartProduct(cartProduct)
        }
    }

    override suspend fun getAllCartProducts(): Resource<List<CartProduct>> =
        withContext(Dispatchers.IO) {
            try {
                val cartProducts = dao.getAllCartProducts()
                Resource.Success(data = cartProducts)
            } catch (e: Exception) {
                Resource.Error(message = e.message ?: "Uknown error")
            }
        }

    override suspend fun deleteProduct(cartProduct: CartProduct) {
        withContext(Dispatchers.IO) {
            dao.delete(cartProduct)
        }
    }

    override suspend fun deleteAllCartProducts() {
        withContext(Dispatchers.IO) {
            dao.deleteAllCartProducts()
        }
    }
}