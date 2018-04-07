package com.itome.githubmvi.ui.events.core

import com.itome.githubmvi.data.model.Event
import com.itome.githubmvi.data.model.User
import com.itome.githubmvi.mvibase.MviResult

sealed class EventsResult : MviResult {

    sealed class FetchFirstPageResult : EventsResult() {
        data class Success(val events: List<Event>) : FetchFirstPageResult()
        data class Failure(val error: Throwable) : FetchFirstPageResult()
        object InFlight : FetchFirstPageResult()
    }

    sealed class FetchEventsPageResult : EventsResult() {
        data class Success(val events: List<Event>) : FetchEventsPageResult()
        data class Failure(val error: Throwable) : FetchEventsPageResult()
        object InFlight : FetchEventsPageResult()
    }

    sealed class FetchLoginUserResult : EventsResult() {
        data class Success(val user: User) : FetchLoginUserResult()
        data class Failure(val error: Throwable) : FetchLoginUserResult()
        object InFlight : FetchLoginUserResult()
    }
}