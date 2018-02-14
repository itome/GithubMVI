package com.itome.githubmvi.di.component

import com.itome.githubmvi.di.module.LoginActivityModule
import com.itome.githubmvi.ui.login.LoginActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(LoginActivityModule::class)])
interface LoginActivityComponent {
    fun inject(activity: LoginActivity)
}
