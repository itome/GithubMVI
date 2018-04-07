package com.itome.githubmvi.di.component

import com.itome.githubmvi.di.module.ApiModule
import com.itome.githubmvi.di.module.OkHttpModule
import com.itome.githubmvi.di.module.RepositoryActivityModule
import com.itome.githubmvi.di.module.SchedulerModule
import com.itome.githubmvi.ui.repository.RepositoryActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        (RepositoryActivityModule::class),
        (ApiModule::class),
        (OkHttpModule::class),
        (SchedulerModule::class)
    ]
)
interface RepositoryActivityComponent {
    fun inject(activity: RepositoryActivity)
}
