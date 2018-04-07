package com.itome.githubmvi.ui.login.core

import com.itome.githubmvi.data.repository.LoginRepository
import com.itome.githubmvi.extensions.pairWithDelay
import com.itome.githubmvi.scheduler.SchedulerProvider
import com.itome.githubmvi.ui.login.core.LoginAction.FetchAccessTokenAction
import com.itome.githubmvi.ui.login.core.LoginAction.FetchLoginDataAction
import com.itome.githubmvi.ui.login.core.LoginResult.FetchAccessTokenResult
import com.itome.githubmvi.ui.login.core.LoginResult.FetchLoginDataResult
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import javax.inject.Inject

class LoginProcessorHolder @Inject constructor(
        private val repository: LoginRepository,
        private val schedulerProvider: SchedulerProvider
) {

    private val fetchAccessTokenProcessor =
            ObservableTransformer<FetchAccessTokenAction, FetchAccessTokenResult> { actions ->
                actions.flatMap { action ->
                    repository.fetchAccessToken(action.clientId, action.clientSecret, action.code)
                            .flatMap { repository.fetchLoginUser() }
                            .toObservable()
                            .flatMap {
                                pairWithDelay(1L,
                                        FetchAccessTokenResult.Success(it.name, it.avatar_url),
                                        FetchAccessTokenResult.StartNextActivity
                                )
                            }
                            .cast(FetchAccessTokenResult::class.java)
                            .onErrorReturn(FetchAccessTokenResult::Failure)
                            .subscribeOn(schedulerProvider.io())
                            .observeOn(schedulerProvider.ui())
                            .startWith(FetchAccessTokenResult.InFlight)
                }
            }

    private val fetchLoginDataProcessor =
            ObservableTransformer<FetchLoginDataAction, FetchLoginDataResult> { actions ->
                actions.flatMap {
                    repository.readAccessToken()
                            .toObservable()
                            .flatMap { accessToken ->
                                if (accessToken == "") {
                                    Observable.just(FetchLoginDataResult.NeedsAccessToken)
                                } else {
                                    repository.getLoginUser()
                                            .toObservable()
                                            .flatMap {
                                                pairWithDelay(1L,
                                                        FetchLoginDataResult.Success(it.name, it.avatar_url),
                                                        FetchLoginDataResult.StartNextActivity
                                                )
                                            }
                                }
                            }
                            .cast(FetchLoginDataResult::class.java)
                            .onErrorReturn(FetchLoginDataResult::Failure)
                            .subscribeOn(schedulerProvider.io())
                            .observeOn(schedulerProvider.ui())
                            .startWith(FetchLoginDataResult.InFlight)
                }
            }

    internal var actionProcessor =
            ObservableTransformer<LoginAction, LoginResult> { actions ->
                actions.publish { shared ->
                    Observable.merge(
                            shared.ofType(FetchAccessTokenAction::class.java).compose(fetchAccessTokenProcessor),
                            shared.ofType(FetchLoginDataAction::class.java).compose(fetchLoginDataProcessor)
                    ).mergeWith(
                            shared.filter({ v ->
                                v !is FetchAccessTokenAction
                                        && v != FetchLoginDataAction
                            }).flatMap({ w ->
                                Observable.error<LoginResult>(
                                        IllegalArgumentException("Unknown Action type: $w"))
                            })
                    )
                }
            }
}