package com.vc.onlinestore.domain.usecases.firebase

import com.google.firebase.auth.FirebaseUser
import com.vc.onlinestore.data.firebase.dto.User
import com.vc.onlinestore.domain.repository.FirebaseRepository
import com.vc.onlinestore.utils.Resource
import javax.inject.Inject

class CreateAccountWithEmailAndPassword @Inject constructor(
    private val repository: FirebaseRepository
) {

    suspend operator fun invoke(user: User, password: String): Resource<FirebaseUser> =
        repository.createAccountWithEmailAndPassword(user = user, password = password)

}