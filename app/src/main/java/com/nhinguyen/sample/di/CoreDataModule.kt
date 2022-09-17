package com.nhinguyen.sample.di

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
class CoreDataModule {

    @Singleton
    @Provides
    internal fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .build()
    }

    @Provides
    @Singleton
    fun provideMoshiConverterFactory(moshi: Moshi): MoshiConverterFactory =
        MoshiConverterFactory.create(moshi)

}