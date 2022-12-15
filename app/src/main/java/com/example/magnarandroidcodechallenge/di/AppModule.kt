package com.example.magnarandroidcodechallenge.di

import android.content.Context
import android.content.SharedPreferences
import com.example.magnarandroidcodechallenge.api.RetrofitService
import com.example.magnarandroidcodechallenge.repository.MagnarRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private const val BASE_URL = "https://tsaenrollmentbyidemia.tsa.dhs.gov/ccl/"

    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideRetrofitService(retrofit: Retrofit): RetrofitService {
        return retrofit.create(RetrofitService::class.java)
    }

    @Provides
    fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
    }

    @Provides
    fun provideFileDir(@ApplicationContext context: Context): File {
        return context.filesDir
    }
}