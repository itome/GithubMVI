package com.itome.githubmvi.ui.events

import com.itome.githubmvi.mvibase.MviAction

sealed class EventsAction : MviAction {

    object FetchEventsAction : EventsAction()

    data class FetchEventsPageAction(
            val pageNum: Int
    ) : EventsAction()
}