package com.itome.githubmvi.data.datastore.remote

import com.itome.githubmvi.data.datastore.ApiService
import com.itome.githubmvi.data.model.Repository
import com.itome.githubmvi.data.model.User
import io.reactivex.Single
import javax.inject.Inject

class UserRemoteDateStore @Inject constructor(
        private val apiService: ApiService
) {

    fun fetchUser(
            accessToken: String, userName: String
    ): Single<User> = apiService.getUser("token " + accessToken, userName)

    fun fetchUserRepos(
            accessToken: String, userName: String
    ): Single<List<Repository>> = apiService.getUserRepos("token " + accessToken, userName)

}