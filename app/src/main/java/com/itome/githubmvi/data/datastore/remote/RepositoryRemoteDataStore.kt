package com.itome.githubmvi.data.datastore.remote

import com.itome.githubmvi.data.datastore.ApiService
import com.itome.githubmvi.data.model.Readme
import com.itome.githubmvi.data.model.Repository
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject

class RepositoryRemoteDataStore @Inject constructor(
    private val apiService: ApiService,
    private val okHttpClient: OkHttpClient
) {

    fun fetchRepository(
        accessToken: String, repoName: String
    ): Single<Repository> {
        val pair = getOwnerAndRepoName(repoName)
        return apiService.getRepository("token " + accessToken, pair.first, pair.second)
    }

    fun fetchReadme(
        accessToken: String, repoName: String
    ): Single<Readme> {
        val pair = getOwnerAndRepoName(repoName)
        return apiService.getReadme("token " + accessToken, pair.first, pair.second)
    }

    fun checkIsStarred(
        accessToken: String, repoName: String
    ): Single<Boolean> {
        val pair = getOwnerAndRepoName(repoName)
        val request = Request.Builder()
            .url("https://api.github.com/user/starred/${pair.first}/${pair.second}")
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

    fun starRepository(
        accessToken: String, repoName: String
    ): Completable {
        val pair = getOwnerAndRepoName(repoName)
        return apiService.starRepository("token " + accessToken, pair.first, pair.second)
    }

    fun unStarRepository(
        accessToken: String, repoName: String
    ): Completable {
        val pair = getOwnerAndRepoName(repoName)
        return apiService.unStarRepository("token " + accessToken, pair.first, pair.second)
    }

    private fun getOwnerAndRepoName(fullName: String): Pair<String, String> {
        val slashIndex = fullName.indexOf('/')
        val ownerName = fullName.substring(0, slashIndex)
        val repoName = fullName.substring(slashIndex + 1, fullName.length)
        return ownerName to repoName
    }
}