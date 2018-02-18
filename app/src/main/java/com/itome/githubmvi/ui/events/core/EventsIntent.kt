package com.itome.githubmvi.ui.events.core

import com.itome.githubmvi.mvibase.MviIntent

sealed class EventsIntent : MviIntent {

    object FetchFirstPageIntent : EventsIntent()

    data class FetchPageIntent(val pageNum: Int) : EventsIntent()

    object FetchLoginUserIntent: EventsIntent()
}