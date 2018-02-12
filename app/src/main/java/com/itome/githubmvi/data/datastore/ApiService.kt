package com.itome.githubmvi.data.datastore

import com.itome.githubmvi.data.model.User
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path


interface ApiService {

    @GET("user")
    fun getUser(): Single<User>

    @GET("users/{username}")
    fun getUser(@Path("username") user: String): Single<User>
}