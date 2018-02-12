package com.itome.githubmvi.ui.splash

import com.itome.githubmvi.mvibase.MviAction

sealed class LoginAction : MviAction {

    data class FetchAccessTokenAction(
            val clientId: String,
            val clientSecret: String,
            val code: String
    ) : LoginAction()

    object FetchLoginDataAction : LoginAction()
}