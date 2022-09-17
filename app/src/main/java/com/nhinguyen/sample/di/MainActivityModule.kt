package com.nhinguyen.sample.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import com.nhinguyen.sample.ui.main.MainActivity

@Module
abstract class MainActivityModule {
    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeMainActivity(): MainActivity

}
