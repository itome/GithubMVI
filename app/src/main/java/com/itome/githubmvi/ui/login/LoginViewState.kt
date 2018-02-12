package com.itome.githubmvi.ui.login

import com.itome.githubmvi.mvibase.MviViewState

data class LoginViewState(
        val userName: String,
        val userImageUrl: String,
        val needsAccessToken: Boolean,
        val isLoading: Boolean,
        val error: Throwable?
) : MviViewState {
    companion object {
        fun idle(): LoginViewState {
            return LoginViewState(
                    userName = "",
                    userImageUrl = "",
                    needsAccessToken = false,
                    isLoading = false,
                    error = null
            )
        }
    }
}