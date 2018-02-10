package com.itome.githubmvi.splash

import com.itome.githubmvi.mvibase.MviIntent

sealed class SplashIntent : MviIntent {

    object InitialIntent : SplashIntent()

    data class FetchAccessTokenIntent(
            val clientId: String,
            val clientSecret: String,
            val code: String
    ) : SplashIntent()

    data class FetchSelfDataIntent(val accessToken: String) : SplashIntent()
}
