package com.itome.githubmvi.splash

import com.itome.githubmvi.data.User
import com.itome.githubmvi.mvibase.MviResult

sealed class SplashResult : MviResult {

    sealed class DoNothingResult : SplashResult() {
        object Success : DoNothingResult()
        data class Failure(val error: Throwable) : DoNothingResult()
    }

    sealed class FetchAccessTokenResult : SplashResult() {
        data class Success(val accessToken: String) : FetchAccessTokenResult()
        data class Failure(val error: Throwable) : FetchAccessTokenResult()
        object InFlight : FetchAccessTokenResult()
    }

    sealed class FetchSelfDataResult: SplashResult() {
        data class Success(val user: User) : FetchSelfDataResult()
        data class Failure(val error: Throwable) : FetchSelfDataResult()
        object InFlight : FetchSelfDataResult()
    }
}