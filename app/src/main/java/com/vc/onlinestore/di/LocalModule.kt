package com.vc.onlinestore.di

import android.content.Context
import androidx.room.Room
import com.vc.onlinestore.data.local.CartProductDao
import com.vc.onlinestore.data.local.ProductDatabase
import com.vc.onlinestore.utils.Constants.PRODUCT_DATABASE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Provides
    @Singleton
    fun provideProductDatabase(@ApplicationContext context: Context): ProductDatabase =
        Room.databaseBuilder(
            context, ProductDatabase::class.java, PRODUCT_DATABASE
        ).build()

    @Provides
    @Singleton
    fun provideProductApi(database: ProductDatabase): CartProductDao =
        database.cartProductDao()
}