package com.itome.githubmvi.ui.events.core

import com.itome.githubmvi.data.model.Event
import com.itome.githubmvi.data.model.User
import com.itome.githubmvi.mvibase.MviViewState

data class EventsViewState(
        val loginUser: User?,
        val events: List<Event>,
        val error: Throwable?,
        val nextPage: Int,
        val isLoading: Boolean
) : MviViewState {
    companion object {
        fun idle(): EventsViewState {
            return EventsViewState(
                    loginUser = null,
                    events = emptyList(),
                    error = null,
                    nextPage = 1,
                    isLoading = false
            )
        }
    }
}