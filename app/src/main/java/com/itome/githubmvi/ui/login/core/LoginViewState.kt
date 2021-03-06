package com.itome.githubmvi.ui.login.core

import com.itome.githubmvi.mvibase.MviViewState

data class LoginViewState(
    val userName: String,
    val userImageUrl: String,
    val needsAccessToken: Boolean,
    val isLoading: Boolean,
    val error: Throwable?,
    val startNextActivity: Boolean
) : MviViewState {
    companion object {
        fun idle(): LoginViewState {
            return LoginViewState(
                userName = "",
                userImageUrl = "",
                needsAccessToken = false,
                isLoading = false,
                error = null,
                startNextActivity = false
            )
        }
    }
}