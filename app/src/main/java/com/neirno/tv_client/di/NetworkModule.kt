package com.neirno.tv_client.di

import com.neirno.tv_client.data.api.interceptors.DynamicUrlInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideDynamicUrlInterceptor(): DynamicUrlInterceptor {
        return DynamicUrlInterceptor()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(interceptor: DynamicUrlInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("http://default.com") // здесь вы можете указать стандартный URL, он будет заменен интерцептором при наличии
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}