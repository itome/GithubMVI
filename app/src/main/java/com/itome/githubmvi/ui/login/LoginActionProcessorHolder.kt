package com.itome.githubmvi.ui.login

import com.itome.githubmvi.data.repository.LoginRepository
import com.itome.githubmvi.extensions.pairWithDelay
import com.itome.githubmvi.ui.login.LoginResult.FetchAccessTokenResult
import com.itome.githubmvi.ui.login.LoginResult.FetchLoginDataResult
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class LoginActionProcessorHolder @Inject constructor(
        private val repository: LoginRepository
) {

    private val fetchAccessTokenProcessor =
            ObservableTransformer<LoginAction.FetchAccessTokenAction, FetchAccessTokenResult> { actions ->
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
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .startWith(FetchAccessTokenResult.InFlight)
                }
            }

    private val fetchLoginDataProcessor =
            ObservableTransformer<LoginAction.FetchLoginDataAction, LoginResult.FetchLoginDataResult> { actions ->
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
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .startWith(FetchLoginDataResult.InFlight)
                }
            }

    internal var actionProcessor =
            ObservableTransformer<LoginAction, LoginResult> { actions ->
                actions.publish { shared ->
                    Observable.merge(
                            shared.ofType(LoginAction.FetchAccessTokenAction::class.java).compose(fetchAccessTokenProcessor),
                            shared.ofType(LoginAction.FetchLoginDataAction::class.java).compose(fetchLoginDataProcessor)
                    ).mergeWith(
                            shared.filter({ v ->
                                v !is LoginAction.FetchAccessTokenAction
                                        && v != LoginAction.FetchLoginDataAction
                            }).flatMap({ w ->
                                Observable.error<LoginResult>(
                                        IllegalArgumentException("Unknown Action type: $w"))
                            })
                    )
                }
            }
}