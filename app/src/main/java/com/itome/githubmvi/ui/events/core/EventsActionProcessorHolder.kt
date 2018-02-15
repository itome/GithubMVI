package com.itome.githubmvi.ui.events.core

import com.itome.githubmvi.data.repository.EventsRepository
import com.itome.githubmvi.scheduler.SchedulerProvider
import com.itome.githubmvi.ui.events.core.EventsAction.FetchEventsAction
import com.itome.githubmvi.ui.events.core.EventsAction.FetchEventsPageAction
import com.itome.githubmvi.ui.events.core.EventsResult.FetchEventsPageResult
import com.itome.githubmvi.ui.events.core.EventsResult.FetchEventsResult
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import javax.inject.Inject

class EventsActionProcessorHolder @Inject constructor(
        private val repository: EventsRepository,
        private val schedulerProvider: SchedulerProvider
) {

    private val fetchEventsProcessor =
            ObservableTransformer<FetchEventsAction, FetchEventsResult> { actions ->
                actions.flatMap {
                    repository.getEvents()
                            .toObservable()
                            .map { events -> FetchEventsResult.Success(events) }
                            .cast(FetchEventsResult::class.java)
                            .onErrorReturn(FetchEventsResult::Failure)
                            .subscribeOn(schedulerProvider.io())
                            .observeOn(schedulerProvider.ui())
                            .startWith(FetchEventsResult.InFlight)
                }
            }

    private val fetchEventsPageIntent =
            ObservableTransformer<FetchEventsPageAction, FetchEventsPageResult> { actions ->
                actions.flatMap {
                    repository.getEvents()
                            .toObservable()
                            .map { events -> FetchEventsPageResult.Success(events) }
                            .cast(FetchEventsPageResult::class.java)
                            .onErrorReturn(FetchEventsPageResult::Failure)
                            .subscribeOn(schedulerProvider.io())
                            .observeOn(schedulerProvider.ui())
                            .startWith(FetchEventsPageResult.InFlight)
                }
            }

    internal var actionProcessor =
            ObservableTransformer<EventsAction, EventsResult> { actions ->
                actions.publish { shared ->
                    Observable.merge(
                            shared.ofType(FetchEventsAction::class.java).compose(fetchEventsProcessor),
                            shared.ofType(FetchEventsPageAction::class.java).compose(fetchEventsPageIntent)
                    ).mergeWith(
                            shared.filter({ v ->
                                v != FetchEventsAction
                                        && v !is FetchEventsPageAction
                            }).flatMap({ w ->
                                Observable.error<EventsResult>(
                                        IllegalArgumentException("Unknown Action type: $w")
                                )
                            })
                    )
                }
            }
}