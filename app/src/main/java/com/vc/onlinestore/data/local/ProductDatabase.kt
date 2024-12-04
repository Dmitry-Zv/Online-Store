package com.vc.onlinestore.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.vc.onlinestore.domain.model.CartProduct
import com.vc.onlinestore.utils.Converters

@Database(entities = [CartProduct::class], version = 1)
@TypeConverters(Converters::class)
abstract class ProductDatabase : RoomDatabase() {
    abstract fun cartProductDao(): CartProductDao
}