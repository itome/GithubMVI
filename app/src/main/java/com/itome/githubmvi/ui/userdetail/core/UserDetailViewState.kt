package com.itome.githubmvi.ui.userdetail.core

import com.itome.githubmvi.data.model.Repository
import com.itome.githubmvi.data.model.User
import com.itome.githubmvi.mvibase.MviViewState

data class UserDetailViewState(
        val user: User?,
        val repos: List<Repository>?,
        val error: Throwable?,
        val isLoading: Boolean
): MviViewState {
    companion object {
        fun idle(): UserDetailViewState {
            return UserDetailViewState(
                    user = null,
                    repos = null,
                    error = null,
                    isLoading = false
            )
        }
    }
}