package com.nhinguyen.sample.ui.base

import com.nhinguyen.sample.di.Injectable
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

abstract class HasSupportFragmentInjectorActivity : BaseActivity(), HasAndroidInjector, Injectable {
    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector() = fragmentInjector
}
