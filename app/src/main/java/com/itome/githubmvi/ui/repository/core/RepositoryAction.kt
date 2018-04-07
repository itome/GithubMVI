package com.itome.githubmvi.ui.repository.core

import com.itome.githubmvi.mvibase.MviAction

sealed class RepositoryAction : MviAction {

    data class FetchRepositoryAction(val repoName: String) : RepositoryAction()

    data class FetchReadmeAction(val repoName: String) : RepositoryAction()

    data class CheckIsStarredAction(val repoName: String) : RepositoryAction()

    data class StarAction(val repoName: String) : RepositoryAction()

    data class UnStarAction(val repoName: String) : RepositoryAction()
}