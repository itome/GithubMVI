package com.itome.githubmvi.ui.splash

import com.itome.githubmvi.data.model.User
import com.itome.githubmvi.mvibase.MviViewState

data class SplashViewState(
        val user: User?,
        val needsAccessToken: Boolean,
        val isLoading: Boolean,
        val error: Throwable?
) : MviViewState {
    companion object {
        fun idle(): SplashViewState {
            return SplashViewState(
                    user = null,
                    needsAccessToken = false,
                    isLoading = false,
                    error = null
            )
        }
    }
}