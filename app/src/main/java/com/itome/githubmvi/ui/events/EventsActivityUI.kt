package com.itome.githubmvi.ui.events

import android.support.v7.widget.LinearLayoutManager
import com.itome.githubmvi.ui.events.core.EventsViewState
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.frameLayout
import org.jetbrains.anko.recyclerview.v7.recyclerView

class EventsActivityUI : AnkoComponent<EventsActivity> {

    private val eventsAdapter = EventsAdapter()

    fun applyState(state: EventsViewState) {
        eventsAdapter.events = state.events
        eventsAdapter.notifyDataSetChanged()
    }

    override fun createView(ui: AnkoContext<EventsActivity>) = with(ui) {
        frameLayout {
            recyclerView {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter = eventsAdapter
            }
        }
    }
}
