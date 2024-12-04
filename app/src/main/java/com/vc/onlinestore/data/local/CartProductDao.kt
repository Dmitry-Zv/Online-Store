package com.vc.onlinestore.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vc.onlinestore.domain.model.CartProduct

@Dao
interface CartProductDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartProduct(cartProduct: CartProduct)

    @Query("SELECT * FROM cart_product")
    suspend fun getAllCartProducts(): List<CartProduct>

    @Delete
    suspend fun delete(cartProduct: CartProduct)

    @Query("DELETE FROM cart_product")
    suspend fun deleteAllCartProducts()
}