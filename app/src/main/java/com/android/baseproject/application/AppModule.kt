package com.cavice.customer.application

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/*
*Created by NhiNguyen on 8/27/2019.
*/

@Module(
    includes = [
        AndroidSupportInjectionModule::class
    ]
)
class AppModule {

    @Provides
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }
}