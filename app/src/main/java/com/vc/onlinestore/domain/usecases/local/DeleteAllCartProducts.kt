package com.vc.onlinestore.domain.usecases.local

import com.vc.onlinestore.domain.repository.LocalRepository
import javax.inject.Inject

class DeleteAllCartProducts @Inject constructor(
    private val repository: LocalRepository
) {

    suspend operator fun invoke() =
        repository.deleteAllCartProducts()
}