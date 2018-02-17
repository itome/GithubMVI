package com.itome.githubmvi.data.repository

import com.itome.githubmvi.data.datastore.local.LoginLocalDataStore
import com.itome.githubmvi.data.datastore.remote.UserRemoteDateStore
import com.itome.githubmvi.data.model.Repository
import com.itome.githubmvi.data.model.User
import io.reactivex.Single
import javax.inject.Inject

class UserRepository @Inject constructor(
        private val loginLocalDataStore: LoginLocalDataStore,
        private val remoteDataStore: UserRemoteDateStore
) {

    fun getUser(userName: String): Single<User> {
        return loginLocalDataStore.readAccessToken()
                .flatMap { accessToken ->
                    remoteDataStore.fetchUser(accessToken, userName)
                }
    }

    fun getUserRepos(userName: String): Single<List<Repository>> {
        return loginLocalDataStore.readAccessToken()
                .flatMap { accessToken ->
                    remoteDataStore.fetchUserRepos(accessToken, userName)
                }
    }

    fun checkIsFollowed(userName: String): Single<Boolean> {
        return loginLocalDataStore.readAccessToken()
                .flatMap { accessToken ->
                    remoteDataStore.checkIsFollowed(accessToken, userName)
                }
    }
}