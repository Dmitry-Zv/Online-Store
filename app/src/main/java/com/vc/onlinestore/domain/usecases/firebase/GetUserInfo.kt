package com.vc.onlinestore.domain.usecases.firebase

import com.vc.onlinestore.data.firebase.dto.User
import com.vc.onlinestore.domain.repository.FirebaseRepository
import com.vc.onlinestore.utils.Resource
import javax.inject.Inject

class GetUserInfo @Inject constructor(
    private val repository: FirebaseRepository
) {

    suspend operator fun invoke(): Resource<User> =
        repository.getUserInfo()
}