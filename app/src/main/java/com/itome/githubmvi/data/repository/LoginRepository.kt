package com.itome.githubmvi.data.repository

import com.itome.githubmvi.data.datastore.local.LoginLocalDataStore
import com.itome.githubmvi.data.datastore.remote.LoginRemoteDataStore
import com.itome.githubmvi.data.model.User
import io.reactivex.Single

class LoginRepository(
        private val localDataStore: LoginLocalDataStore,
        private val remoteDataStore: LoginRemoteDataStore
) {

    fun readAccessToken(): Single<String> {
        return localDataStore.readAccessToken()
    }

    fun fetchAccessToken(clientId: String, clientSecret: String, code: String): Single<String> {
        return remoteDataStore.fetchAccessToken(clientId, clientSecret, code)
                .flatMapCompletable { localDataStore.saveAccessToken(it) }
                .andThen(localDataStore.readAccessToken())
    }

    fun getLoginUser(): Single<User> {
        return localDataStore.readLoginUser()
                .onErrorResumeNext(fetchLoginUser())
    }

    fun fetchLoginUser(): Single<User> {
        return localDataStore.readAccessToken()
                .flatMap { accessToken -> remoteDataStore.fetchLoginUser(accessToken) }
                .flatMapCompletable { user -> localDataStore.saveLoginUser(user) }
                .andThen(localDataStore.readLoginUser())
    }
}