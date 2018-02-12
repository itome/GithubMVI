package com.itome.githubmvi.ui.login

import com.itome.githubmvi.mvibase.MviIntent

sealed class LoginIntent : MviIntent {

    data class FetchAccessTokenIntent(
            val clientId: String,
            val clientSecret: String,
            val code: String
    ) : LoginIntent()

    object FetchLoginDataIntent : LoginIntent()
}
