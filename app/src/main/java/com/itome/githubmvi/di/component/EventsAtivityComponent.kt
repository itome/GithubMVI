package com.itome.githubmvi.di.component

import com.itome.githubmvi.di.module.ApiModule
import com.itome.githubmvi.di.module.EventsActivityModule
import com.itome.githubmvi.di.module.SchedulerModule
import com.itome.githubmvi.ui.events.EventsActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    (EventsActivityModule::class),
    (ApiModule::class),
    (SchedulerModule::class)]
)
interface EventsAtivityComponent {
    fun inject(activity: EventsActivity)
}
