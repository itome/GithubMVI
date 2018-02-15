package com.itome.githubmvi.data.datastore.remote

import com.itome.githubmvi.data.datastore.ApiService
import com.itome.githubmvi.data.model.Event
import io.reactivex.Single
import javax.inject.Inject

class EventsRemoteDataStore @Inject constructor(
        private val apiService: ApiService
) {
    fun fetchEvents(accessToken: String): Single<List<Event>> = apiService.getEvents(accessToken)
}