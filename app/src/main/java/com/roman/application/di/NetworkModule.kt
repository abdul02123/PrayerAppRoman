package com.roman.application.di

import com.roman.application.data.remote.PhotoApi
import com.roman.application.util.Constant.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Provides
    fun providesRetrofit():Retrofit{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
    }

    @Provides
    fun providePhotoApi(retrofit: Retrofit): PhotoApi {
        return retrofit.create(PhotoApi::class.java)
    }
}