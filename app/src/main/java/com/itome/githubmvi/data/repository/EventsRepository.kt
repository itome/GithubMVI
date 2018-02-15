package com.itome.githubmvi.data.repository

import com.itome.githubmvi.data.datastore.local.LoginLocalDataStore
import com.itome.githubmvi.data.datastore.remote.EventsRemoteDataStore
import com.itome.githubmvi.data.model.Event
import io.reactivex.Single
import javax.inject.Inject

class EventsRepository @Inject constructor(
        private val loginLocalDataStore: LoginLocalDataStore,
        private val remoteDataStore: EventsRemoteDataStore
) {

    fun getEvents(): Single<List<Event>> {
        return loginLocalDataStore.readAccessToken()
                .flatMap { accessToken ->
                    remoteDataStore.fetchEvents(accessToken)
                }
    }

}