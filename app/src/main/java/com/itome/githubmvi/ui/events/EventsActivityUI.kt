package com.itome.githubmvi.ui.events

import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.frameLayout

class EventsActivityUI : AnkoComponent<EventsActivity> {
    override fun createView(ui: AnkoContext<EventsActivity>) = with(ui) {
        frameLayout()
    }
}
