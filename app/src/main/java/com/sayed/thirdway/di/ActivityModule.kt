package com.sayed.thirdway.di

import com.sayed.thirdway.ui.home.LoginFragment
import com.sayed.thirdway.ui.home.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
public abstract class ActivityModule {

    /**
     * Inject activities to be able to request dependencies
     */
    @ContributesAndroidInjector(modules = [LoginModule::class])
    abstract fun contributeMainActivity() : MainActivity


    @Module
    interface LoginModule {
        @ContributesAndroidInjector
        fun contributeLoginFragment(): LoginFragment
    }

}