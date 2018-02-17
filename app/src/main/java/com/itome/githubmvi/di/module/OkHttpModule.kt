package com.itome.githubmvi.di.module

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
class OkHttpModule {

    @Singleton
    @Provides
    fun provideOkHttpClicent(): OkHttpClient = OkHttpClient()

}
