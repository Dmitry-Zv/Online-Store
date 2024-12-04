package com.vc.onlinestore.domain.usecases.local

import com.vc.onlinestore.domain.model.CartProduct
import com.vc.onlinestore.domain.repository.LocalRepository
import javax.inject.Inject

class DeleteCartProduct @Inject constructor(
    private val repository: LocalRepository
) {
    suspend operator fun invoke(cartProduct: CartProduct) =
        repository.deleteProduct(cartProduct)
}