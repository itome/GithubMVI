package com.itome.githubmvi.di.module

import com.itome.githubmvi.mvibase.MviViewModel
import com.itome.githubmvi.ui.repository.core.RepositoryActionProcessorHolder
import com.itome.githubmvi.ui.repository.core.RepositoryIntent
import com.itome.githubmvi.ui.repository.core.RepositoryViewModel
import com.itome.githubmvi.ui.repository.core.RepositoryViewState
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryActivityModule {

    @Singleton
    @Provides
    fun provideViewModel(
            processorHolder: RepositoryActionProcessorHolder
    ): MviViewModel<RepositoryIntent, RepositoryViewState> = RepositoryViewModel(processorHolder)

}
