package com.vc.onlinestore.domain.usecases.firebase

import com.vc.onlinestore.data.firebase.dto.order.Order
import com.vc.onlinestore.domain.repository.FirebaseRepository
import com.vc.onlinestore.utils.Resource
import javax.inject.Inject

class GetAllOrdersByUser @Inject constructor(
    private val repository: FirebaseRepository
) {
    suspend operator fun invoke(): Resource<List<Order>> =
        repository.getAllOrdersByUser()
}