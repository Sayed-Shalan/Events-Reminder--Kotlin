package com.sayed.thirdway.app

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.sayed.thirdway.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class App : Application(), HasActivityInjector {


    //Dec Data
    @Inject
    lateinit var dispatchingAndroidActivityInjector: DispatchingAndroidInjector<Activity>


    /**
     * Start On App Create *************************
     */
    override fun onCreate() {
        super.onCreate()

        DaggerAppComponent.builder().application(this).build().inject(this) //inject this
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun activityInjector(): AndroidInjector<Activity> {
        return this.dispatchingAndroidActivityInjector
    }
}