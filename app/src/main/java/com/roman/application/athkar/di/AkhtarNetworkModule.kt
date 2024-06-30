package com.roman.application.athkar.di

import com.roman.application.athkar.data.remote.AkhtarApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@InstallIn(SingletonComponent::class)
@Module
class AkhtarNetworkModule {

    @Provides
    fun providePhotoApi(retrofit: Retrofit): AkhtarApi {
        return retrofit.create(AkhtarApi::class.java)
    }
}