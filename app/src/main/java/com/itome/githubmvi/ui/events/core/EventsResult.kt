package com.itome.githubmvi.ui.events.core

import com.itome.githubmvi.data.model.Event
import com.itome.githubmvi.mvibase.MviResult

sealed class EventsResult : MviResult {

    sealed class FetchEventsResult: EventsResult() {
        data class Success(val events: List<Event>) : FetchEventsResult()
        data class Failure(val error: Throwable) : FetchEventsResult()
        object InFlight : FetchEventsResult()
    }

    sealed class FetchEventsPageResult: EventsResult() {
        data class Success(val events: List<Event>) : FetchEventsPageResult()
        data class Failure(val error: Throwable) : FetchEventsPageResult()
        object InFlight : FetchEventsPageResult()
    }
}