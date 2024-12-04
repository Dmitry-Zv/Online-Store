package com.vc.onlinestore.domain.repository

import android.graphics.Bitmap
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.vc.onlinestore.data.firebase.dto.Address
import com.vc.onlinestore.data.firebase.dto.User
import com.vc.onlinestore.data.firebase.dto.order.Order
import com.vc.onlinestore.utils.Resource

interface FirebaseRepository {

    suspend fun createAccountWithEmailAndPassword(
        user: User,
        password: String
    ): Resource<FirebaseUser>

    suspend fun login(email: String, password: String): Resource<FirebaseUser>

    suspend fun logout():Resource<Unit>

    suspend fun resetPassword(email: String): Resource<String>

    suspend fun saveUserInfo(userUid: String, user: User): Resource<Unit>

    suspend fun getUserInfo(): Resource<User>

    suspend fun updateUserInfo(user:User, shouldRetrieveOldImage: Boolean, image:Bitmap?):Resource<User>

    suspend fun saveAddress(address: Address): Resource<Unit>

    suspend fun deleteAddress(address: Address): Resource<Unit>

    suspend fun getUserAddresses(): Resource<List<Address>>

    suspend fun placeOrder(order: Order): Resource<Order>

    suspend fun getAllOrdersByUser():Resource<List<Order>>
}