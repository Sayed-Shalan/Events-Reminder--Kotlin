package com.sayed.thirdway.di

import android.content.Context
import com.sayed.thirdway.app.App
import com.sayed.thirdway.utils.SPUtils
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    //provide singleton context
    @Singleton
    @Provides
    fun provideContext(app : App) : Context{
        return app;
    }

    //provide SPUtils object
    @Provides
    fun provideSPUtils(app : Context): SPUtils {
        return SPUtils(app)
    }

}