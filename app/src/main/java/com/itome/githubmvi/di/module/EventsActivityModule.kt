package com.itome.githubmvi.di.module

import com.itome.githubmvi.mvibase.MviViewModel
import com.itome.githubmvi.ui.events.EventsActionProcessorHolder
import com.itome.githubmvi.ui.events.EventsIntent
import com.itome.githubmvi.ui.events.EventsViewModel
import com.itome.githubmvi.ui.events.EventsViewState
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
