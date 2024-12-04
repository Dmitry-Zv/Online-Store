package com.vc.onlinestore.di

import android.content.Context
import android.content.SharedPreferences
import com.vc.onlinestore.utils.Constants.ONLINE_STORE_SP
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SharedPrefModule {

    @Provides
    @Singleton
    fun provideSharedPref(@ApplicationContext context:Context):SharedPreferences=
        context.getSharedPreferences(ONLINE_STORE_SP, Context.MODE_PRIVATE)
}