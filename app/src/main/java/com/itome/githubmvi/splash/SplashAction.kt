package com.itome.githubmvi.splash

import com.itome.githubmvi.mvibase.MviAction

sealed class SplashAction : MviAction {

    object DoNothingAction : SplashAction()

    data class FetchAccessTokenAction(
            val clientId: String,
            val clientSecret: String,
            val code: String
    ) : SplashAction()

    data class FetchSelfDataAction(val accessToken: String) : SplashAction()
}