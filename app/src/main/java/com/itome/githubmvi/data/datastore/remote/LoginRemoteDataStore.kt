package com.itome.githubmvi.data.datastore.remote

import com.itome.githubmvi.data.datastore.ApiService
import com.itome.githubmvi.data.datastore.LoginService
import com.itome.githubmvi.data.model.User
import io.reactivex.Single
import javax.inject.Inject

class LoginRemoteDataStore @Inject constructor(
    private val apiService: ApiService,
    private val loginService: LoginService
) {

    fun fetchAccessToken(clientId: String, clientSecret: String, code: String): Single<String> =
        loginService.getAccessTokenData(code, clientId, clientSecret).map { it.access_token }

    fun fetchLoginUser(accessToken: String): Single<User> =
        apiService.getUser("token " + accessToken)
}