package com.itome.githubmvi.data.datastore.remote

import com.itome.githubmvi.data.datastore.ApiService
import com.itome.githubmvi.data.model.Repository
import com.itome.githubmvi.data.model.User
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject

class UserRemoteDateStore @Inject constructor(
        private val apiService: ApiService,
        private val okHttpClient: OkHttpClient
) {

    fun fetchUser(
            accessToken: String, userName: String
    ): Single<User> = apiService.getUser("token " + accessToken, userName)

    fun fetchUserRepos(
            accessToken: String, userName: String
    ): Single<List<Repository>> = apiService.getUserRepos("token " + accessToken, userName)

    fun checkIsFollowed(
            accessToken: String, userName: String
    ): Single<Boolean> {
        val request = Request.Builder()
                .url("https://api.github.com/user/following/$userName")
                .header("Authorization", "token " + accessToken)
                .build()
        return Single.create<Boolean> {
            try {
                val response = okHttpClient.newCall(request).execute()
                it.onSuccess(response.code() == 204)
            } catch (e: Throwable) {
                it.onError(e)
            }
        }
    }

}