package com.itome.githubmvi.ui.repository.core

import com.itome.githubmvi.data.model.Readme
import com.itome.githubmvi.data.model.Repository
import com.itome.githubmvi.mvibase.MviResult

sealed class RepositoryResult: MviResult {

    sealed class FetchRepositoryResult: RepositoryResult() {
        data class Success(val repository: Repository): FetchRepositoryResult()
        data class Failure(val error: Throwable): FetchRepositoryResult()
        object InFlight: FetchRepositoryResult()
    }

    sealed class FetchReadmeResult: RepositoryResult() {
        data class Success(val readme: Readme): FetchReadmeResult()
        data class Failure(val error: Throwable): FetchReadmeResult()
        object InFlight: FetchReadmeResult()
    }

    sealed class CheckIsStarredResult: RepositoryResult() {
        data class Success(val isStarred: Boolean): CheckIsStarredResult()
        data class Failure(val error: Throwable): CheckIsStarredResult()
        object InFlight: CheckIsStarredResult()
    }

    sealed class StarResult: RepositoryResult() {
        object Success: StarResult()
        data class Failure(val error: Throwable): StarResult()
    }

    sealed class UnStarResult: RepositoryResult() {
        object Success: UnStarResult()
        data class Failure(val error: Throwable): UnStarResult()
    }
}