package com.itome.githubmvi.ui.splash

import com.itome.githubmvi.mvibase.MviViewState

data class SplashViewState(
        val userName: String,
        val userImageUrl: String,
        val needsAccessToken: Boolean,
        val isLoading: Boolean,
        val error: Throwable?
) : MviViewState {
    companion object {
        fun idle(): SplashViewState {
            return SplashViewState(
                    userName = "",
                    userImageUrl = "",
                    needsAccessToken = false,
                    isLoading = false,
                    error = null
            )
        }
    }
}