package com.itome.githubmvi.data.repository

import com.itome.githubmvi.data.datastore.local.LoginLocalDataStore
import com.itome.githubmvi.data.datastore.remote.LoginRemoteDataStore
import com.itome.githubmvi.data.model.User
import io.reactivex.Completable
import io.reactivex.Single

class LoginRepository(
        private val localDataStore: LoginLocalDataStore,
        private val remoteDataStore: LoginRemoteDataStore
) {

    fun readAccessToken(): Single<String> {
        return localDataStore.readAccessToken()
    }

    fun fetchAccessToken(clientId: String, clientSecret: String, code: String): Completable {
        return remoteDataStore.fetchAccessToken(clientId, clientSecret, code)
                .flatMapCompletable {
                    return@flatMapCompletable localDataStore.saveAccessToken(it)
                }
    }

    fun fetchLoginUser(): Single<User> {
        return readAccessToken()
                .flatMap { accessToken ->
                    remoteDataStore.fetchLoginUser(accessToken)
                }
    }
}