package com.itome.githubmvi.data.datastore

import com.itome.githubmvi.data.model.AccessTokenData
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface LoginService {

    @Headers("Accept: application/json")
    @GET("login/oauth/access_token")
    fun getAccessTokenData(
        @Query("code") code: String,
        @Query("client_id") clientId: String,
        @Query("client_secret") clientSecret: String
    ): Single<AccessTokenData>
}