package com.itome.githubmvi.data.datastore

import com.itome.githubmvi.data.model.Event
import com.itome.githubmvi.data.model.User
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path


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
}