package com.itome.githubmvi.ui.login.core

import com.itome.githubmvi.data.repository.LoginRepository
import com.itome.githubmvi.extensions.pairWithDelay
import com.itome.githubmvi.mvibase.MviProcessorHolder
import com.itome.githubmvi.scheduler.SchedulerProvider
import com.itome.githubmvi.ui.login.core.LoginAction.FetchAccessTokenAction
import com.itome.githubmvi.ui.login.core.LoginAction.FetchLoginDataAction
import com.itome.githubmvi.ui.login.core.LoginResult.FetchAccessTokenResult
import com.itome.githubmvi.ui.login.core.LoginResult.FetchLoginDataResult
import io.reactivex.Observable
import javax.inject.Inject

class LoginProcessorHolder @Inject constructor(
    private val repository: LoginRepository,
    private val schedulerProvider: SchedulerProvider
) : MviProcessorHolder<LoginAction, LoginResult>() {

    private val fetchAccessTokenProcessor =
        createProcessor<FetchAccessTokenAction, FetchAccessTokenResult> { action ->
            repository.fetchAccessToken(action.clientId, action.clientSecret, action.code)
                .flatMap { repository.fetchLoginUser() }
                .toObservable()
                .flatMap {
                    pairWithDelay(
                        1L,
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

    private val fetchLoginDataProcessor =
        createProcessor<FetchLoginDataAction, FetchLoginDataResult> { action ->
            repository.readAccessToken()
                .toObservable()
                .flatMap { accessToken ->
                    if (accessToken == "") {
                        Observable.just(FetchLoginDataResult.NeedsAccessToken)
                    } else {
                        repository.getLoginUser()
                            .toObservable()
                            .flatMap {
                                pairWithDelay(
                                    1L,
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

    override val actionProcessor = mergeProcessor(
        fetchAccessTokenProcessor to FetchAccessTokenAction::class,
        fetchLoginDataProcessor to FetchLoginDataAction::class
    )
}