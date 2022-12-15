package com.example.magnarandroidcodechallenge.di

import com.example.magnarandroidcodechallenge.api.RetrofitService
import com.example.magnarandroidcodechallenge.repository.MagnarRepository
import com.example.magnarandroidcodechallenge.repository.MagnarRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class ViewModelModule {
    @Provides
    fun provideRepository(service: RetrofitService): MagnarRepository {
        return MagnarRepositoryImpl(service)
    }
}