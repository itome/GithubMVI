package com.itome.githubmvi.ui.splash

import com.itome.githubmvi.mvibase.MviResult

sealed class LoginResult : MviResult {

    sealed class FetchAccessTokenResult : LoginResult() {
        object Success : FetchAccessTokenResult()
        data class Failure(val error: Throwable) : FetchAccessTokenResult()
        object InFlight : FetchAccessTokenResult()
    }

    sealed class FetchLoginDataResult : LoginResult() {
        data class Success(val userName: String, val userImageUrl: String) : FetchLoginDataResult()
        data class Failure(val error: Throwable) : FetchLoginDataResult()
        object NeedsAccessToken : FetchLoginDataResult()
        object InFlight : FetchLoginDataResult()
    }
}