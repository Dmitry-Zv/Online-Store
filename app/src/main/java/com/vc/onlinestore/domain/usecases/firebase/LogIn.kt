package com.vc.onlinestore.domain.usecases.firebase

import com.google.firebase.auth.FirebaseUser
import com.vc.onlinestore.domain.repository.FirebaseRepository
import com.vc.onlinestore.utils.Resource
import javax.inject.Inject

class LogIn @Inject constructor(
    private val repository: FirebaseRepository
) {

    suspend operator fun invoke(
        email: String, password: String
    ): Resource<FirebaseUser> =
        repository.login(email, password)
}