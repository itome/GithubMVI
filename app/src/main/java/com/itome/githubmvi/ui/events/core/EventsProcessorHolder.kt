package com.itome.githubmvi.ui.events.core

import com.itome.githubmvi.data.repository.EventsRepository
import com.itome.githubmvi.data.repository.LoginRepository
import com.itome.githubmvi.scheduler.SchedulerProvider
import com.itome.githubmvi.ui.events.core.EventsAction.*
import com.itome.githubmvi.ui.events.core.EventsResult.*
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import javax.inject.Inject

class EventsProcessorHolder @Inject constructor(
        private val repository: EventsRepository,
        private val loginRepository: LoginRepository,
        private val schedulerProvider: SchedulerProvider
) {

    private val fetchFirstPageProcessor =
            ObservableTransformer<FetchFirstPageAction, FetchFirstPageResult> { actions ->
                actions.flatMap {
                    repository.getEvents(1)
                            .toObservable()
                            .map { user -> FetchFirstPageResult.Success(user) }
                            .cast(FetchFirstPageResult::class.java)
                            .onErrorReturn(FetchFirstPageResult::Failure)
                            .subscribeOn(schedulerProvider.io())
                            .observeOn(schedulerProvider.ui())
                            .startWith(FetchFirstPageResult.InFlight)
                }
            }

    private val fetchEventsPageProcessor =
            ObservableTransformer<FetchEventsPageAction, FetchEventsPageResult> { actions ->
                actions.flatMap { action ->
                    repository.getEvents(action.pageNum)
                            .toObservable()
                            .map { events -> FetchEventsPageResult.Success(events) }
                            .cast(FetchEventsPageResult::class.java)
                            .onErrorReturn(FetchEventsPageResult::Failure)
                            .subscribeOn(schedulerProvider.io())
                            .observeOn(schedulerProvider.ui())
                            .startWith(FetchEventsPageResult.InFlight)
                }
            }

    private val fetchLoginUserProcessor =
            ObservableTransformer<FetchLoginUserAction, FetchLoginUserResult> { actions ->
                actions.flatMap {
                    loginRepository.getLoginUser()
                            .toObservable()
                            .map { user -> FetchLoginUserResult.Success(user) }
                            .cast(FetchLoginUserResult::class.java)
                            .onErrorReturn(FetchLoginUserResult::Failure)
                            .subscribeOn(schedulerProvider.io())
                            .observeOn(schedulerProvider.ui())
                            .startWith(FetchLoginUserResult.InFlight)
                }
            }

    internal var actionProcessor =
            ObservableTransformer<EventsAction, EventsResult> { actions ->
                actions.publish { shared ->
                    Observable.merge(
                            shared.ofType(FetchFirstPageAction::class.java).compose(fetchFirstPageProcessor),
                            shared.ofType(FetchEventsPageAction::class.java).compose(fetchEventsPageProcessor),
                            shared.ofType(FetchLoginUserAction::class.java).compose(fetchLoginUserProcessor)
                    ).mergeWith(
                            shared.filter({ v ->
                                v != FetchFirstPageAction &&
                                        v !is FetchEventsPageAction &&
                                        v != FetchLoginUserAction
                            }).flatMap({ w ->
                                Observable.error<EventsResult>(
                                        IllegalArgumentException("Unknown Action type: $w")
                                )
                            })
                    )
                }
            }
}