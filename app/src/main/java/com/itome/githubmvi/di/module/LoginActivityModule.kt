package com.itome.githubmvi.di.module

import com.itome.githubmvi.mvibase.MviViewModel
import com.itome.githubmvi.ui.login.LoginActionProcessorHolder
import com.itome.githubmvi.ui.login.LoginIntent
import com.itome.githubmvi.ui.login.LoginViewModel
import com.itome.githubmvi.ui.login.LoginViewState
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class LoginActivityModule {

    @Singleton
    @Provides
    fun provideViewModel(
            processorHolder: LoginActionProcessorHolder
    ): MviViewModel<LoginIntent, LoginViewState> = LoginViewModel(processorHolder)

}