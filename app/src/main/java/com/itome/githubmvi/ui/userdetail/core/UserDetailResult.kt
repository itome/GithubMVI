package com.itome.githubmvi.ui.userdetail.core

import com.itome.githubmvi.data.model.Repository
import com.itome.githubmvi.data.model.User
import com.itome.githubmvi.mvibase.MviResult

sealed class UserDetailResult: MviResult {

    sealed class FetchUserResult: UserDetailResult() {
        data class Success(val user: User): FetchUserResult()
        data class Failure(val error: Throwable?): FetchUserResult()
        object InFlight: FetchUserResult()
    }

    sealed class FetchUserReposResult: UserDetailResult() {
        data class Success(val repos: List<Repository>): FetchUserReposResult()
        data class Feilure(val error: Throwable?): FetchUserReposResult()
        object InFlight: FetchUserReposResult()
    }
}