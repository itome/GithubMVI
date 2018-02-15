package com.itome.githubmvi.di.module

import com.itome.githubmvi.mvibase.MviViewModel
import com.itome.githubmvi.ui.events.core.EventsActionProcessorHolder
import com.itome.githubmvi.ui.events.core.EventsIntent
import com.itome.githubmvi.ui.events.core.EventsViewModel
import com.itome.githubmvi.ui.events.core.EventsViewState
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class EventsActivityModule {

    @Singleton
    @Provides
    fun provideViewModel(
            processorHolder: EventsActionProcessorHolder
    ): MviViewModel<EventsIntent, EventsViewState> = EventsViewModel(processorHolder)

}
