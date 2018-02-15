package com.itome.githubmvi.di.module

import com.itome.githubmvi.scheduler.AndroidSchedulerProvider
import com.itome.githubmvi.scheduler.SchedulerProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class SchedulerModule {

    @Singleton
    @Provides
    fun provideSchedulerProvider(): SchedulerProvider = AndroidSchedulerProvider
}