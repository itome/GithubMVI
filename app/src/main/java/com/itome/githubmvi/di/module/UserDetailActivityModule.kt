package com.itome.githubmvi.di.module

import com.itome.githubmvi.mvibase.MviViewModel
import com.itome.githubmvi.ui.userdetail.core.UserDetailProcessorHolder
import com.itome.githubmvi.ui.userdetail.core.UserDetailIntent
import com.itome.githubmvi.ui.userdetail.core.UserDetailViewModel
import com.itome.githubmvi.ui.userdetail.core.UserDetailViewState
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class UserDetailActivityModule {

    @Singleton
    @Provides
    fun provideViewModel(
            processorHolder: UserDetailProcessorHolder
    ): MviViewModel<UserDetailIntent, UserDetailViewState> = UserDetailViewModel(processorHolder)

}