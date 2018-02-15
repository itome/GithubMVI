package com.itome.githubmvi.ui.events.core

import com.itome.githubmvi.mvibase.MviViewModel
import com.itome.githubmvi.ui.events.core.EventsResult.*
import com.itome.githubmvi.ui.events.core.EventsIntent.*
import com.itome.githubmvi.ui.events.core.EventsAction.*
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class EventsViewModel @Inject constructor(
        private val actionProcessorHolder: EventsActionProcessorHolder
) : MviViewModel<EventsIntent, EventsViewState> {

    private val intentsSubject: PublishSubject<EventsIntent> = PublishSubject.create()
    private val statesObservable: Observable<EventsViewState> = compose()

    override fun processIntents(intents: Observable<EventsIntent>) {
        intents.subscribe(intentsSubject)
    }

    override fun states(): Observable<EventsViewState> = statesObservable

    private fun compose(): Observable<EventsViewState> {
        return intentsSubject
                .map(this::actionFromIntent)
                .compose(actionProcessorHolder.actionProcessor)
                .scan(EventsViewState.idle(), reducer)
                .replay(1)
                .autoConnect(0)
    }

    private fun actionFromIntent(intent: EventsIntent): EventsAction {
        return when (intent) {
            FetchEventsIntent -> FetchEventsAction
            is FetchEventsPageIntent -> FetchEventsPageAction(intent.pageNum)
        }
    }

    companion object {
        private val reducer = { previousState: EventsViewState, result: EventsResult ->
            when (result) {
                is FetchEventsResult -> when (result) {
                    is FetchEventsResult.Success ->
                        previousState.copy(
                                events = result.events,
                                error = null,
                                isLoading = false
                        )
                    is FetchEventsResult.Failure ->
                        previousState.copy(error = result.error)
                    FetchEventsResult.InFlight ->
                        previousState.copy(isLoading = true)
                }

                is FetchEventsPageResult -> when (result) {
                    is FetchEventsPageResult.Success ->
                        previousState.copy(
                                events = result.events,
                                error = null,
                                isLoading = false
                        )
                    is FetchEventsPageResult.Failure ->
                        previousState.copy(error = result.error)

                    FetchEventsPageResult.InFlight ->
                        previousState.copy(isLoading = true)
                }
            }
        }
    }
}