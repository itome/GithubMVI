package com.itome.githubmvi.di.module

import com.itome.githubmvi.data.datastore.local.LoginLocalDataStore
import com.itome.githubmvi.data.datastore.remote.LoginRemoteDataStore
import com.itome.githubmvi.data.repository.LoginRepository
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

    @Provides
    fun provideLoginLocalDataStore() = LoginLocalDataStore()

    @Provides
    fun provideLoginRemoteDataStore() = LoginRemoteDataStore()

    @Provides
    fun provideLoginRepository(
            localDataStore: LoginLocalDataStore,
            remoteDataStore: LoginRemoteDataStore
    ) = LoginRepository(localDataStore, remoteDataStore)

    @Provides
    fun provideLoginActionProcessorHolder(
            repository: LoginRepository
    ) = LoginActionProcessorHolder(repository)

    @Singleton
    @Provides
    fun provideViewModel(
            processorHolder: LoginActionProcessorHolder
    ): MviViewModel<LoginIntent, LoginViewState> = LoginViewModel(processorHolder)
}