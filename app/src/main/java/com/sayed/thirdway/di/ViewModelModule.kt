package com.sayed.thirdway.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sayed.thirdway.view_model.LoginViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
public abstract class ViewModelModule {

    //Provide Factory
    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    /**
     * Provide View models
     */
    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLoginViewModel(loginViewModel: LoginViewModel): ViewModel
}