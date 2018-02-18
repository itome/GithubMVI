package com.itome.githubmvi.ui.repository.core

import com.itome.githubmvi.data.model.Readme
import com.itome.githubmvi.data.model.Repository
import com.itome.githubmvi.mvibase.MviViewState

data class RepositoryViewState(
        val repository: Repository?,
        val readme: Readme?,
        val isStarred: Boolean,
        val error: Throwable?,
        val isLoading: Boolean
): MviViewState {
    companion object {
        fun idel(): RepositoryViewState {
            return RepositoryViewState(
                    repository = null,
                    readme = null,
                    isStarred = false,
                    error = null,
                    isLoading = false
            )
        }
    }
}