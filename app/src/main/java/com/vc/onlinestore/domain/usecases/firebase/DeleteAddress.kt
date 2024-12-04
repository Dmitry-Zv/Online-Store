package com.vc.onlinestore.domain.usecases.firebase

import com.vc.onlinestore.data.firebase.dto.Address
import com.vc.onlinestore.domain.repository.FirebaseRepository
import com.vc.onlinestore.utils.Resource
import javax.inject.Inject

class DeleteAddress @Inject constructor(
    private val repository: FirebaseRepository
) {

    suspend operator fun invoke(address: Address): Resource<Unit> =
        repository.deleteAddress(address)
}