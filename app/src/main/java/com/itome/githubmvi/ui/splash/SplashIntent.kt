package com.itome.githubmvi.ui.splash

import com.itome.githubmvi.mvibase.MviIntent

sealed class SplashIntent : MviIntent {

    data class FetchAccessTokenIntent(
            val clientId: String,
            val clientSecret: String,
            val code: String
    ) : SplashIntent()

    object FetchLoginDataIntent : SplashIntent()
}
