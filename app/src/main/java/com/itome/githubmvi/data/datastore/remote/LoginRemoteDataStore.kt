package com.itome.githubmvi.data.datastore.remote

import com.itome.githubmvi.data.datastore.ApiService
import com.itome.githubmvi.data.datastore.LoginService
import com.itome.githubmvi.data.datastore.RemoteDataStore
import com.itome.githubmvi.data.model.User
import io.reactivex.Single
import javax.inject.Inject

class LoginRemoteDataStore @Inject constructor() : RemoteDataStore() {

    fun fetchAccessToken(clientId: String, clientSecret: String, code: String): Single<String> {
        val baseUrl = "https://github.com/"
        val service = createService(LoginService::class.java, null, baseUrl)
        return service.getAccessTokenData(code, clientId, clientSecret)
                .map { it.access_token }
    }

    fun fetchLoginUser(accessToken: String): Single<User> {
        val service = createService(ApiService::class.java, accessToken)
        return service.getUser()
    }
}