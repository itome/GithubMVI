package com.itome.githubmvi.ui.splash

import com.itome.githubmvi.data.repository.LoginRepository
import com.itome.githubmvi.ui.splash.LoginResult.FetchAccessTokenResult
import com.itome.githubmvi.ui.splash.LoginResult.FetchLoginDataResult
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LoginActionProcessorHolder(
        private val repository: LoginRepository
) {

    private val fetchAccessTokenProcessor =
            ObservableTransformer<LoginAction.FetchAccessTokenAction, FetchAccessTokenResult> { actions ->
                actions.flatMap { action ->
                    repository.fetchAccessToken(action.clientId, action.clientSecret, action.code)
                            .andThen(Observable.just(FetchAccessTokenResult.Success))
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
                    Observable.just(repository.getAccessToken())
                            .flatMap { accessToken ->
                                if (accessToken == "") {
                                    Observable.just(FetchLoginDataResult.NeedsAccessToken)
                                } else {
                                    Observable.just(FetchLoginDataResult.InFlight)
                                }
                            }
                            .cast(FetchLoginDataResult::class.java)
                            .onErrorReturn(FetchLoginDataResult::Failure)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
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