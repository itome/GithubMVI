package com.itome.githubmvi.ui.events.core

import com.itome.githubmvi.data.model.Event
import com.itome.githubmvi.mvibase.MviViewState

data class EventsViewState(
        val events: List<Event>,
        val error: Throwable?,
        val isLoading: Boolean
) : MviViewState {
    companion object {
        fun idle(): EventsViewState {
            return EventsViewState(
                    events = emptyList(),
                    error = null,
                    isLoading = false
            )
        }
    }
}