package com.itome.githubmvi.data.datastore

import com.itome.githubmvi.data.model.Event
import com.itome.githubmvi.data.model.Readme
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
            @Path("username") userName: String,
            @Query("page") pageNum: Int
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

    @GET("repos/{owner}/{repo}")
    fun getRepository(
            @Header("Authorization") accessToken: String,
            @Path("owner") ownerName: String,
            @Path("repo") repoName: String
    ): Single<Repository>

    @GET("repos/{owner}/{repo}/readme")
    fun getReadme(
            @Header("Authorization") accessToken: String,
            @Path("owner") ownerName: String,
            @Path("repo") repoName: String
    ): Single<Readme>

    @PUT("user/starred/{owner}/{repo}")
    @Headers("Content-Length: 0")
    fun starRepository(
            @Header("Authorization") accessToken: String,
            @Path("owner") ownerName: String,
            @Path("repo") repoName: String
    ): Completable

    @DELETE("user/starred/{owner}/{repo}")
    fun unStarRepository(
            @Header("Authorization") accessToken: String,
            @Path("owner") ownerName: String,
            @Path("repo") repoName: String
    ): Completable
}