package com.itome.githubmvi.ui.events.core

import com.itome.githubmvi.mvibase.MviAction

sealed class EventsAction : MviAction {

    object FetchFirstPageAction : EventsAction()

    data class FetchEventsPageAction(
        val pageNum: Int
    ) : EventsAction()

    object FetchLoginUserAction : EventsAction()
}