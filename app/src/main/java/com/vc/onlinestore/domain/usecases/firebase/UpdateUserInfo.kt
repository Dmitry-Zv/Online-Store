package com.vc.onlinestore.domain.usecases.firebase

import android.graphics.Bitmap
import com.vc.onlinestore.data.firebase.dto.User
import com.vc.onlinestore.domain.repository.FirebaseRepository
import com.vc.onlinestore.utils.Resource
import javax.inject.Inject

class UpdateUserInfo @Inject constructor(
    private val repository: FirebaseRepository
) {

    suspend operator fun invoke(
        user: User,
        shouldRetrieveOldImage: Boolean,
        image: Bitmap?
    ): Resource<User> =
        repository.updateUserInfo(
            user,
            shouldRetrieveOldImage,
            image
        )
}