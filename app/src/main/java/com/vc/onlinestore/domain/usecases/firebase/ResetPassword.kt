package com.vc.onlinestore.domain.usecases.firebase

import com.vc.onlinestore.domain.repository.FirebaseRepository
import com.vc.onlinestore.utils.Resource
import javax.inject.Inject

class ResetPassword @Inject constructor(
    private val repository: FirebaseRepository
) {

    suspend operator fun invoke(email: String): Resource<String> =
        repository.resetPassword(email)
}