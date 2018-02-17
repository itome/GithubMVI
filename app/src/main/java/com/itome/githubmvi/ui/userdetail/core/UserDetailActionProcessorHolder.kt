package com.itome.githubmvi.ui.userdetail.core

import android.util.Log
import com.itome.githubmvi.data.repository.UserRepository
import com.itome.githubmvi.scheduler.SchedulerProvider
import com.itome.githubmvi.ui.userdetail.core.UserDetailAction.*
import com.itome.githubmvi.ui.userdetail.core.UserDetailResult.*
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import javax.inject.Inject

class UserDetailActionProcessorHolder @Inject constructor(
        private val repository: UserRepository,
        private val schedulerProvider: SchedulerProvider
) {

    private val fetchUserProcessor =
            ObservableTransformer<FetchUserAction, FetchUserResult> { actions ->
                actions.flatMap { action ->
                    repository.getUser(action.userName)
                            .toObservable()
                            .map { user -> FetchUserResult.Success(user) }
                            .cast(FetchUserResult::class.java)
                            .onErrorReturn(FetchUserResult::Failure)
                            .subscribeOn(schedulerProvider.io())
                            .observeOn(schedulerProvider.ui())
                            .startWith(FetchUserResult.InFlight)
                }
            }

    private val fetchUserReposProcessor =
            ObservableTransformer<FetchUserReposAction, FetchUserReposResult> { actions ->
                actions.flatMap { action ->
                    repository.getUserRepos(action.userName)
                            .toObservable()
                            .map { repos -> FetchUserReposResult.Success(repos) }
                            .cast(FetchUserReposResult::class.java)
                            .onErrorReturn(FetchUserReposResult::Feilure)
                            .subscribeOn(schedulerProvider.io())
                            .observeOn(schedulerProvider.ui())
                            .startWith(FetchUserReposResult.InFlight)
                }
            }

    private val checkIsFollowedProcessor =
            ObservableTransformer<CheckIsFollowedAction, CheckIsFollowedResult> { actions ->
                actions.flatMap { action ->
                    repository.checkIsFollowed(action.userName)
                            .toObservable()
                            .map { isFollowed -> CheckIsFollowedResult.Success(isFollowed) }
                            .cast(CheckIsFollowedResult::class.java)
                            .onErrorReturn(CheckIsFollowedResult::Failure)
                            .subscribeOn(schedulerProvider.io())
                            .observeOn(schedulerProvider.ui())
                            .startWith(CheckIsFollowedResult.InFlight)
                }
            }

    private val followUserProcessor =
            ObservableTransformer<FollowAction, FollowResult> { actions ->
                actions.flatMap { action ->
                    repository.followUser(action.userName)
                            .andThen(Observable.just(FollowResult.Success))
                            .cast(FollowResult::class.java)
                            .onErrorReturn(FollowResult::Failure)
                            .subscribeOn(schedulerProvider.io())
                            .observeOn(schedulerProvider.ui())
                }
            }

    private val unFollowUserProcessor =
            ObservableTransformer<UnFollowAction, UnFollowResult> { actions ->
                actions.flatMap { action ->
                    repository.unFollowUser(action.userName)
                            .doOnComplete {
                                Log.d("Completed", "")
                            }
                            .andThen(Observable.just(UnFollowResult.Success))
                            .cast(UnFollowResult::class.java)
                            .onErrorReturn(UnFollowResult::Failure)
                            .subscribeOn(schedulerProvider.io())
                            .observeOn(schedulerProvider.ui())
                }
            }

    internal var actionProcessor =
            ObservableTransformer<UserDetailAction, UserDetailResult> { actions ->
                actions.publish { shared ->
                    Observable.merge(listOf(
                            shared.ofType(FetchUserAction::class.java).compose(fetchUserProcessor),
                            shared.ofType(FetchUserReposAction::class.java).compose(fetchUserReposProcessor),
                            shared.ofType(CheckIsFollowedAction::class.java).compose(checkIsFollowedProcessor),
                            shared.ofType(FollowAction::class.java).compose(followUserProcessor),
                            shared.ofType(UnFollowAction::class.java).compose(unFollowUserProcessor)
                    )).mergeWith(
                            shared.filter({ v ->
                                v !is FetchUserAction &&
                                        v !is FetchUserReposAction &&
                                        v !is CheckIsFollowedAction &&
                                        v !is FollowAction &&
                                        v !is UnFollowAction
                            }).flatMap({ w ->
                                Observable.error<FetchUserResult>(
                                        IllegalArgumentException("Unknown Action type: $w")
                                )
                            })
                    )
                }
            }
}