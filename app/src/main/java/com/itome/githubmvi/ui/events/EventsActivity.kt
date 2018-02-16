package com.itome.githubmvi.ui.events

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.itome.githubmvi.di.component.DaggerEventsAtivityComponent
import com.itome.githubmvi.di.module.ApiModule
import com.itome.githubmvi.di.module.EventsActivityModule
import com.itome.githubmvi.mvibase.MviView
import com.itome.githubmvi.mvibase.MviViewModel
import com.itome.githubmvi.ui.events.core.EventsIntent
import com.itome.githubmvi.ui.events.core.EventsIntent.FetchEventsIntent
import com.itome.githubmvi.ui.events.core.EventsIntent.FetchEventsPageIntent
import com.itome.githubmvi.ui.events.core.EventsViewState
import com.itome.githubmvi.ui.repository.RepositoryActivity
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import org.jetbrains.anko.setContentView
import org.jetbrains.anko.startActivity
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

        disposable.add(ui.userImageClickPublisher.subscribe(this::showUserDetailActivity))
        disposable.add(ui.itemViewClickPublisher.subscribe(this::showRepositoryActivity))
    }

    override fun onStart() {
        super.onStart()
        bind()
        fetchEventsIntentPublisher.onNext(FetchEventsIntent)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }

    override fun intents(): Observable<EventsIntent> {
        return Observable.merge(
                fetchEventsIntentPublisher,
                fetchEventsPageIntentPublisher
        )
    }

    override fun render(state: EventsViewState) {
        ui.applyState(state)
    }

    private fun bind() {
        disposable.add(viewModel.states().subscribe(this::render))
        viewModel.processIntents(intents())
    }

    private fun showUserDetailActivity(userId: Int) {
        Log.d("ShowUserDetail", "userId: " + userId.toString())
    }

    private fun showRepositoryActivity(repositoryId: Int) {
        startActivity<RepositoryActivity>(RepositoryActivity.REPOSITORY_ID to repositoryId)
    }
}
