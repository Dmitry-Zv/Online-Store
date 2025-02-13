package com.vc.onlinestore.di

import com.vc.onlinestore.data.repository.FirebaseRepositoryImpl
import com.vc.onlinestore.data.repository.LocalRepositoryImpl
import com.vc.onlinestore.data.repository.ProductRepositoryImpl
import com.vc.onlinestore.data.repository.SharedPrefRepositoryImpl
import com.vc.onlinestore.data.repository.TokenRepositoryImpl
import com.vc.onlinestore.domain.repository.FirebaseRepository
import com.vc.onlinestore.domain.repository.LocalRepository
import com.vc.onlinestore.domain.repository.ProductRepository
import com.vc.onlinestore.domain.repository.SharedPrefRepository
import com.vc.onlinestore.domain.repository.TokenRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    @Singleton
    fun bindFirebaseRepositoryImpl_toFirebaseRepository(firebaseRepositoryImpl: FirebaseRepositoryImpl): FirebaseRepository

    @Binds
    @Singleton
    fun bindProductRepositoryImpl_toProductRepository(productRepositoryImpl: ProductRepositoryImpl): ProductRepository

    @Binds
    @Singleton
    fun bindSharedPrefRepositoryImpl_toSharedPrefRepository(sharedPrefRepositoryImpl: SharedPrefRepositoryImpl): SharedPrefRepository

    @Binds
    @Singleton
    fun bindLocalRepositoryImpl_toLocalRepository(localRepositoryImpl: LocalRepositoryImpl): LocalRepository

    @Binds
    @Singleton
    fun bindTokenRepositoryImpl_toTokenRepository(tokenRepositoryImpl: TokenRepositoryImpl): TokenRepository
}