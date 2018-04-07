package com.itome.githubmvi.ui.repository.core

import com.itome.githubmvi.mvibase.MviIntent

sealed class RepositoryIntent : MviIntent {

    data class FetchRepositoryIntent(val repoName: String) : RepositoryIntent()

    data class FetchReadmeIntent(val repoName: String) : RepositoryIntent()

    data class CheckIsStarredIntent(val repoName: String) : RepositoryIntent()

    data class StarIntent(val repoName: String) : RepositoryIntent()

    data class UnStarIntent(val repoName: String) : RepositoryIntent()
}