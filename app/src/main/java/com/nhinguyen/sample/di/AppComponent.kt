package com.nhinguyen.sample.di

import com.nhinguyen.sample.ui.main.MainActivity
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        AppModule::class,
        MainActivityModule::class]
)
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: android.app.Application): Builder

        fun build(): AppComponent
    }

    fun inject(myApplication: com.nhinguyen.sample.MyApplication)

    fun inject(mainActivity: MainActivity)
}