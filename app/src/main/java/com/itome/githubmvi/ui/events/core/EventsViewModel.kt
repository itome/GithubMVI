package com.itome.githubmvi.ui.events.core

import com.itome.githubmvi.mvibase.MviViewModel
import com.itome.githubmvi.ui.events.core.EventsAction.*
import com.itome.githubmvi.ui.events.core.EventsIntent.*
import com.itome.githubmvi.ui.events.core.EventsResult.*
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
            FetchFirstPageIntent -> FetchFirstPageAction
            is FetchPageIntent -> FetchEventsPageAction(intent.pageNum)
            FetchLoginUserIntent -> FetchLoginUserAction
        }
    }

    companion object {
        private val reducer = { previousState: EventsViewState, result: EventsResult ->
            when (result) {
                is FetchFirstPageResult -> when (result) {
                    is FetchFirstPageResult.Success ->
                        previousState.copy(
                                events = result.events,
                                error = null,
                                isLoading = false,
                                nextPage = 2
                        )
                    is FetchFirstPageResult.Failure ->
                        previousState.copy(error = result.error, isLoading = false)
                    FetchFirstPageResult.InFlight ->
                        previousState.copy(isLoading = true)
                }

                is FetchEventsPageResult -> when (result) {
                    is FetchEventsPageResult.Success ->
                        previousState.copy(
                                events = previousState.events + result.events,
                                error = null,
                                isLoading = false,
                                nextPage = previousState.nextPage + 1
                        )
                    is FetchEventsPageResult.Failure ->
                        previousState.copy(error = result.error, isLoading = false)

                    FetchEventsPageResult.InFlight ->
                        previousState.copy(isLoading = true)
                }

                is FetchLoginUserResult -> when (result) {
                    is FetchLoginUserResult.Success ->
                        previousState.copy(
                                loginUser = result.user,
                                error = null,
                                isLoading = false
                        )
                    is FetchLoginUserResult.Failure ->
                        previousState.copy(error = result.error, isLoading = false)
                    FetchLoginUserResult.InFlight ->
                        previousState.copy(isLoading = true)
                }
            }
        }
    }
}