package com.itome.githubmvi.ui.events

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import org.jetbrains.anko.setContentView

class EventsActivity : AppCompatActivity() {

    private val ui by lazy { EventsActivityUI() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui.setContentView(this)
    }
}
