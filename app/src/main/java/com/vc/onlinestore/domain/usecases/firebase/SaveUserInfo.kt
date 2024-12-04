package com.vc.onlinestore.domain.usecases.firebase

import com.vc.onlinestore.data.firebase.dto.User
import com.vc.onlinestore.domain.repository.FirebaseRepository
import com.vc.onlinestore.utils.Resource
import javax.inject.Inject

class SaveUserInfo @Inject constructor(
    private val repository: FirebaseRepository
) {

    suspend operator fun invoke(userUid: String, user: User): Resource<Unit> =
        repository.saveUserInfo(userUid, user)
}