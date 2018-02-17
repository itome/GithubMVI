package com.itome.githubmvi.data.datastore

import com.itome.githubmvi.data.model.Event
import com.itome.githubmvi.data.model.Repository
import com.itome.githubmvi.data.model.User
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.*


interface ApiService {

    @GET("user")
    fun getUser(
            @Header("Authorization") accessToken: String
    ): Single<User>

    @GET("users/{username}")
    fun getUser(
            @Header("Authorization") accessToken: String,
            @Path("username") user: String
    ): Single<User>

    @GET("users/{username}/received_events")
    fun getEvents(
            @Header("Authorization") accessToken: String,
            @Path("username") userName: String
    ): Single<List<Event>>

    @GET("users/{username}/repos")
    fun getUserRepos(
            @Header("Authorization") accessToken: String,
            @Path("username") userName: String
    ): Single<List<Repository>>

    @PUT("user/following/{username}")
    @Headers("Content-Length: 0")
    fun followUser(
            @Header("Authorization") accessToken: String,
            @Path("username") userName: String
    ): Completable

    @DELETE("user/following/{username}")
    fun unFollowUser(
            @Header("Authorization") accessToken: String,
            @Path("username") userName: String
    ): Completable
}