package com.roman.application.base.di

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
class BaseNetworkModule {

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
    fun providesRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
//            .client(okHttpClient)
            .build()
    }
}