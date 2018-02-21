package com.itome.githubmvi.ui.userdetail.core

import com.itome.githubmvi.data.model.Repository
import com.itome.githubmvi.data.model.User
import com.itome.githubmvi.mvibase.MviResult

sealed class UserDetailResult : MviResult {

    sealed class FetchUserResult : UserDetailResult() {
        data class Success(val user: User) : FetchUserResult()
        data class Failure(val error: Throwable?) : FetchUserResult()
        object InFlight : FetchUserResult()
    }

    sealed class FetchUserReposResult : UserDetailResult() {
        data class Success(val repos: List<Repository>) : FetchUserReposResult()
        data class Failure(val error: Throwable?) : FetchUserReposResult()
        object InFlight : FetchUserReposResult()
    }

    sealed class CheckIsLoginUserResult: UserDetailResult() {
        data class Success(val isLoginUser: Boolean) : CheckIsLoginUserResult()
        data class Failure(val error: Throwable?) : CheckIsLoginUserResult()
        object InFlight : CheckIsLoginUserResult()
    }

    sealed class CheckIsFollowedResult : UserDetailResult() {
        data class Success(val isFollowed: Boolean) : CheckIsFollowedResult()
        data class Failure(val error: Throwable?) : CheckIsFollowedResult()
        object InFlight : CheckIsFollowedResult()
    }

    sealed class FollowResult : UserDetailResult() {
        object Success : FollowResult()
        data class Failure(val error: Throwable?) : FollowResult()
    }

    sealed class UnFollowResult : UserDetailResult() {
        object Success : UnFollowResult()
        data class Failure(val error: Throwable?) : UnFollowResult()
    }
}