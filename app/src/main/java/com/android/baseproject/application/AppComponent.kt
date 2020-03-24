package com.cavice.customer.application

import android.app.Application
import com.android.baseproject.application.App
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import javax.inject.Singleton

/*
*Created by NhiNguyen on 8/20/2019.
*/
@Singleton
@Component(modules = [AppModule::class])
interface AppComponent : AndroidInjector<DaggerApplication> {
    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(app: Application): Builder

        fun build(): AppComponent

    }

    fun inject(app: App)

    override fun inject(instance: DaggerApplication)
}