package com.itome.githubmvi.ui.events

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.itome.githubmvi.di.component.DaggerEventsAtivityComponent
import com.itome.githubmvi.di.module.ApiModule
import com.itome.githubmvi.di.module.EventsActivityModule
import com.itome.githubmvi.mvibase.MviView
import com.itome.githubmvi.mvibase.MviViewModel
import com.itome.githubmvi.ui.events.EventsIntent.FetchEventsIntent
import com.itome.githubmvi.ui.events.EventsIntent.FetchEventsPageIntent
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import org.jetbrains.anko.setContentView
import javax.inject.Inject

class EventsActivity : AppCompatActivity(), MviView<EventsIntent, EventsViewState> {

    @Inject
    lateinit var viewModel: MviViewModel<EventsIntent, EventsViewState>

    private val ui by lazy { EventsActivityUI() }

    private val fetchEventsIntentPublisher = PublishSubject.create<FetchEventsIntent>()
    private val fetchEventsPageIntentPublisher = PublishSubject.create<FetchEventsPageIntent>()
    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val component = DaggerEventsAtivityComponent.builder()
                .eventsActivityModule(EventsActivityModule())
                .apiModule(ApiModule())
                .build()
        component.inject(this)

        ui.setContentView(this)
    }

    override fun onStart() {
        super.onStart()
        bind()
        fetchEventsIntentPublisher.onNext(FetchEventsIntent)
    }

    override fun intents(): Observable<EventsIntent> {
        return Observable.merge(
                fetchEventsIntentPublisher,
                fetchEventsPageIntentPublisher
        )
    }

    override fun render(state: EventsViewState) {
        Log.d("EventsViewState", state.toString())
    }

    private fun bind() {
        disposable.add(viewModel.states().subscribe(this::render))
        viewModel.processIntents(intents())
    }

}
