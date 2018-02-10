package com.itome.githubmvi.ui.splash

import com.itome.githubmvi.data.User
import com.itome.githubmvi.mvibase.MviViewState

data class SplashViewState(
        val user: User?,
        val accessToken: String,
        val isLoading: Boolean,
        val error: Throwable?
) : MviViewState {
    companion object {
        fun idle(): SplashViewState {
            return SplashViewState(
                    user = null,
                    accessToken = "",
                    isLoading = false,
                    error = null
            )
        }
    }
}