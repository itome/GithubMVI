package com.itome.githubmvi.di.module

import com.itome.githubmvi.mvibase.MviViewModel
import com.itome.githubmvi.ui.login.core.LoginIntent
import com.itome.githubmvi.ui.login.core.LoginProcessorHolder
import com.itome.githubmvi.ui.login.core.LoginViewModel
import com.itome.githubmvi.ui.login.core.LoginViewState
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class LoginActivityModule {

    @Singleton
    @Provides
    fun provideViewModel(
        processorHolder: LoginProcessorHolder
    ): MviViewModel<LoginIntent, LoginViewState> = LoginViewModel(processorHolder)

}