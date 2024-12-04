package com.vc.onlinestore.domain.usecases.sharedpref

import com.vc.onlinestore.domain.repository.SharedPrefRepository
import javax.inject.Inject

class IsIntroductionScreenWasShown @Inject constructor(
    private val repository: SharedPrefRepository
) {
    operator fun invoke(): Boolean =
        repository.isIntroductionScreenWasShown()
}