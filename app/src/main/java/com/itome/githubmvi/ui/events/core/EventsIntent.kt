package com.itome.githubmvi.ui.events.core

import com.itome.githubmvi.mvibase.MviIntent

sealed class EventsIntent : MviIntent {

    object FetchEventsIntent : EventsIntent()

    data class FetchEventsPageIntent(
            val pageNum: Int
    ) : EventsIntent()
}