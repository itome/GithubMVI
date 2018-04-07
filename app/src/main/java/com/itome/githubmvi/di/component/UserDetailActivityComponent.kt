package com.itome.githubmvi.di.component

import com.itome.githubmvi.di.module.ApiModule
import com.itome.githubmvi.di.module.OkHttpModule
import com.itome.githubmvi.di.module.SchedulerModule
import com.itome.githubmvi.di.module.UserDetailActivityModule
import com.itome.githubmvi.ui.userdetail.UserDetailActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        (UserDetailActivityModule::class),
        (ApiModule::class),
        (OkHttpModule::class),
        (SchedulerModule::class)
    ]
)
interface UserDetailActivityComponent {
    fun inject(activity: UserDetailActivity)
}