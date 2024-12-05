package com.vc.onlinestore.data.repository

import android.graphics.Bitmap
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import com.vc.onlinestore.data.firebase.dto.Address
import com.vc.onlinestore.data.firebase.dto.User
import com.vc.onlinestore.data.firebase.dto.order.Order
import com.vc.onlinestore.domain.repository.FirebaseRepository
import com.vc.onlinestore.utils.Constants.ADDRESS_COLLECTION
import com.vc.onlinestore.utils.Constants.ORDERS_COLLECTION
import com.vc.onlinestore.utils.Constants.PROFILE_IMAGES
import com.vc.onlinestore.utils.Constants.USER_COLLECTION
import com.vc.onlinestore.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.util.UUID
import javax.inject.Inject

class FirebaseRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val storageReference: StorageReference
) : FirebaseRepository {

    override suspend fun createAccountWithEmailAndPassword(
        user: User,
        password: String
    ): Resource<FirebaseUser> = withContext(Dispatchers.IO) {
        try {
            val authResult =
                firebaseAuth.createUserWithEmailAndPassword(user.email, password).await()
            val firebaseUser = checkNotNull(checkNotNull(authResult).user)
            Resource.Success(data = firebaseUser)
        } catch (e: Exception) {
            Resource.Error(message = e.message ?: "Uknown message")
        }
    }

    override suspend fun login(email: String, password: String): Resource<FirebaseUser> =
        withContext(Dispatchers.IO) {
            try {
                val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
                Resource.Success(data = checkNotNull(authResult.user))
            } catch (e: Exception) {
                Resource.Error(message = e.message ?: "Unknown message")
            }
        }

    override suspend fun signInWithGoogle(token: String): Resource<FirebaseUser> =
        withContext(Dispatchers.IO) {
            try {
                val credential = GoogleAuthProvider.getCredential(token, null)
                val authResult = firebaseAuth.signInWithCredential(credential).await()
                val firebaseUser = checkNotNull(checkNotNull(authResult).user)
                Resource.Success(data = firebaseUser)
            } catch (e: Exception) {
                Resource.Error(message = e.message ?: "Unknown message")
            }
        }

    override suspend fun logout(): Resource<Unit> =
        withContext(Dispatchers.IO) {
            try {
                firebaseAuth.currentUser?.let {
                    firebaseAuth.signOut()
                    Resource.Success(Unit)
                } ?: Resource.Error("User is null")
            } catch (e: Exception) {
                Resource.Error(message = e.message ?: "Unknown message")
            }
        }

    override suspend fun resetPassword(email: String): Resource<String> =
        withContext(Dispatchers.IO) {
            try {
                firebaseAuth.sendPasswordResetEmail(email).await()
                Resource.Success(email)
            } catch (e: Exception) {
                Resource.Error(message = e.message ?: "Unknown message")
            }
        }

    override suspend fun saveUserInfo(userUid: String, user: User): Resource<Unit> =
        withContext(Dispatchers.IO) {
            try {
                db.collection(USER_COLLECTION)
                    .document(userUid)
                    .set(user).await()
                Resource.Success(Unit)
            } catch (e: Exception) {
                Resource.Error(message = e.message ?: "Unknown message")
            }
        }

    override suspend fun getUserInfo(): Resource<User> =
        withContext(Dispatchers.IO) {
            try {
                val snapShotDocument = db.collection(USER_COLLECTION)
                    .document(firebaseAuth.uid!!)
                    .get()
                    .await()
                val user = checkNotNull(snapShotDocument.toObject(User::class.java))
                Resource.Success(user)
            } catch (e: Exception) {
                Resource.Error(message = e.message ?: "Unknown error")
            }
        }

    override suspend fun updateUserInfo(
        user: User,
        shouldRetrieveOldImage: Boolean,
        image: Bitmap?
    ): Resource<User> =
        withContext(Dispatchers.IO) {
            try {
                var imageUrl: String? = null
                if (image != null) {
                    val byteArrayOutPutStream = ByteArrayOutputStream()
                    image.compress(Bitmap.CompressFormat.JPEG, 96, byteArrayOutPutStream)
                    val imageByteArray = byteArrayOutPutStream.toByteArray()
                    val imageDirectory =
                        storageReference.child("$PROFILE_IMAGES/${firebaseAuth.uid}/${UUID.randomUUID()}")
                    val result = imageDirectory.putBytes(imageByteArray).await()
                    imageUrl = result.storage.downloadUrl.await().toString()
                }
                db.runTransaction { transaction ->
                    val documentRef = db.collection(USER_COLLECTION).document(firebaseAuth.uid!!)
                    val currentUser = transaction.get(documentRef).toObject(User::class.java)
                    if (shouldRetrieveOldImage) {
                        val newUser = user.copy(imagePath = currentUser?.imagePath ?: "")
                        transaction.set(documentRef, newUser)
                    } else {
                        if (imageUrl != null) {
                            transaction.set(documentRef, user.copy(imagePath = imageUrl))
                        } else {
                            transaction.set(documentRef, user)
                        }
                    }
                }.await()
                Resource.Success(user)
            } catch (e: Exception) {
                Resource.Error(message = e.message ?: "Unknown error")
            }
        }

    override suspend fun saveAddress(address: Address): Resource<Unit> =
        withContext(Dispatchers.IO) {
            try {
                val uid = firebaseAuth.uid
                    ?: return@withContext Resource.Error("User is not authenticated")
                val documentReference = db.collection(USER_COLLECTION)
                    .document(uid)
                    .collection(ADDRESS_COLLECTION)
                    .document()
                address.id = documentReference.id
                documentReference.set(address).await()
                Resource.Success(Unit)
            } catch (e: Exception) {
                Resource.Error(message = e.message ?: "Unknown message")
            }
        }

    override suspend fun deleteAddress(address: Address): Resource<Unit> =
        withContext(Dispatchers.IO) {
            try {
                val uid = firebaseAuth.uid
                    ?: return@withContext Resource.Error("User is not authenticated")
                db.collection(USER_COLLECTION)
                    .document(uid)
                    .collection(ADDRESS_COLLECTION)
                    .document(address.id)
                    .delete()
                    .await()
                Resource.Success(Unit)
            } catch (e: Exception) {
                Resource.Error(message = e.message ?: "Unknown message")
            }
        }


    override suspend fun getUserAddresses(): Resource<List<Address>> =
        withContext(Dispatchers.IO) {
            try {
                val snapshotDocument = db.collection(USER_COLLECTION)
                    .document(firebaseAuth.uid!!)
                    .collection(ADDRESS_COLLECTION)
                    .get().await()
                val address = snapshotDocument.toObjects(Address::class.java)
                Resource.Success(address)
            } catch (e: Exception) {
                Resource.Error(message = e.message ?: "Uknown message")
            }
        }

    override suspend fun placeOrder(order: Order): Resource<Order> =
        withContext(Dispatchers.IO) {
            try {
                db.runBatch {
                    db.collection(USER_COLLECTION)
                        .document(firebaseAuth.uid!!)
                        .collection(ORDERS_COLLECTION)
                        .document()
                        .set(order)

                    db.collection(ORDERS_COLLECTION)
                        .document()
                        .set(order)
                }.await()
                Resource.Success(order)
            } catch (e: Exception) {
                Resource.Error(message = e.message ?: "Unknown message")
            }
        }

    override suspend fun getAllOrdersByUser(): Resource<List<Order>> =
        withContext(Dispatchers.IO) {
            try {
                val snapShotDocument = db.collection(USER_COLLECTION)
                    .document(firebaseAuth.uid!!)
                    .collection(ORDERS_COLLECTION)
                    .get().await()
                val orders = snapShotDocument.toObjects(Order::class.java)
                Resource.Success(orders)
            } catch (e: Exception) {
                Log.e("ORDERS", e.message.toString(), e)
                Resource.Error(message = e.message ?: "Unknown message")
            }
        }

}