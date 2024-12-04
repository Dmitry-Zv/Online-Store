package com.vc.onlinestore.domain.repository

import com.vc.onlinestore.domain.model.CartProduct
import com.vc.onlinestore.utils.Resource

interface LocalRepository {

    suspend fun insertCartProduct(cartProduct: CartProduct)

    suspend fun getAllCartProducts(): Resource<List<CartProduct>>

    suspend fun deleteProduct(cartProduct: CartProduct)

    suspend fun deleteAllCartProducts()
}