package com.itome.githubmvi.data.repository

import com.itome.githubmvi.data.datastore.local.LoginLocalDataStore
import com.itome.githubmvi.data.datastore.remote.LoginRemoteDataStore
import io.reactivex.Completable

class LoginRepository(
        private val localDataStore: LoginLocalDataStore,
        private val remoteDataStore: LoginRemoteDataStore
) {

    fun getAccessToken(): String {
        return localDataStore.getAccessToken()
    }

    fun fetchAccessToken(clientId: String, clientSecret: String, code: String): Completable {
        return remoteDataStore.fetchAccessToken(clientId, clientSecret, code)
                .flatMapCompletable { localDataStore.saveAccessToken(it) }
    }
}