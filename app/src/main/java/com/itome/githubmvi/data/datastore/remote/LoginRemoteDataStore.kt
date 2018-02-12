package com.itome.githubmvi.data.datastore.remote

import com.google.gson.Gson
import com.itome.githubmvi.data.model.AccessTokenData
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class LoginRemoteDataStore {

    fun fetchAccessToken(clientId: String, clientSecret: String, code: String): Single<String> {
        return Single.create {
            val url = "https://github.com/login/oauth/access_token?" +
                    "code=$code&client_id=$clientId&client_secret=$clientSecret"
            val request = Request.Builder()
                    .header("Accept", "application/json")
                    .url(url).build()

            try {
                val responseBody = OkHttpClient().newCall(request).execute().body()!!.string()
                val tokenData = Gson().fromJson(responseBody, AccessTokenData::class.java)
                it.onSuccess(tokenData.access_token)
            } catch (e: IOException) {
                it.onError(e)
            }
        }
    }
}