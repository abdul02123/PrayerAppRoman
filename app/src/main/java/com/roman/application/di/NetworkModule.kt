package com.roman.application.di

import com.roman.application.home.data.remote.HomeApi
import com.roman.application.network.DecryptionInterceptor
import com.roman.application.util.Constant.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Provides
    fun provideDecryptionInterceptor(): DecryptionInterceptor{
        return DecryptionInterceptor("rLiXl7zb3jYAm8ccHzrbT463JVdWwkFX6B1sPqbyms5LYHVvg7qyiMas2Bso8yu8")
    }

    @Provides
    fun provideOkHttpClient(decryptionInterceptor: DecryptionInterceptor): OkHttpClient{
        return OkHttpClient.Builder()
            .addInterceptor(decryptionInterceptor)
            .build()
    }

    @Provides
    fun providesRetrofit(okHttpClient: OkHttpClient):Retrofit{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
//            .client(okHttpClient)
            .build()
    }

    @Provides
    fun providePhotoApi(retrofit: Retrofit): HomeApi {
        return retrofit.create(HomeApi::class.java)
    }
}