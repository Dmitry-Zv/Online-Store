package com.vc.onlinestore.domain.usecases.firebase

import com.vc.onlinestore.domain.repository.FirebaseRepository
import com.vc.onlinestore.utils.Resource
import javax.inject.Inject

class Logout @Inject constructor(
    private val repository: FirebaseRepository
) {
    suspend operator fun invoke(): Resource<Unit> =
        repository.logout()
}