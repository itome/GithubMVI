package com.itome.githubmvi.data.repository

import com.itome.githubmvi.data.datastore.local.LoginLocalDataStore
import com.itome.githubmvi.data.datastore.remote.RepositoryRemoteDataStore
import com.itome.githubmvi.data.model.Readme
import com.itome.githubmvi.data.model.Repository
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class ReposRepository @Inject constructor(
        private val loginLocalDataStore: LoginLocalDataStore,
        private val remoteDataStore: RepositoryRemoteDataStore
) {

    fun getRepository(repoName: String): Single<Repository> {
        return loginLocalDataStore.readAccessToken()
                .flatMap { accessToken ->
                    remoteDataStore.fetchRepository(accessToken, repoName)
                }
    }

    fun getReadme(repoName: String): Single<Readme> {
        return loginLocalDataStore.readAccessToken()
                .flatMap { accessToken ->
                    remoteDataStore.fetchReadme(accessToken, repoName)
                }
    }

    fun checkIsStarred(repoName: String): Single<Boolean> {
        return loginLocalDataStore.readAccessToken()
                .flatMap { accessToken ->
                    remoteDataStore.checkIsStarred(accessToken, repoName)
                }
    }

    fun starRepository(repoName: String): Completable {
        return loginLocalDataStore.readAccessToken()
                .flatMapCompletable { accessToken ->
                    remoteDataStore.starRepository(accessToken, repoName)
                }
    }

    fun unStarRepository(repoName: String): Completable {
        return loginLocalDataStore.readAccessToken()
                .flatMapCompletable { accessToken ->
                    remoteDataStore.unStarRepository(accessToken, repoName)
                }
    }
}