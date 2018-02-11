package com.itome.githubmvi.ui.splash

import com.itome.githubmvi.data.repository.LoginRepository
import com.itome.githubmvi.ui.splash.SplashResult.FetchAccessTokenResult
import com.itome.githubmvi.ui.splash.SplashResult.FetchLoginDataResult
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SplashActionProcessorHolder(
        private val repository: LoginRepository
) {

    private val fetchAccessTokenProcessor =
            ObservableTransformer<SplashAction.FetchAccessTokenAction, FetchAccessTokenResult> { actions ->
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
            ObservableTransformer<SplashAction.FetchLoginDataAction, SplashResult.FetchLoginDataResult> { actions ->
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
            ObservableTransformer<SplashAction, SplashResult> { actions ->
                actions.publish { shared ->
                    Observable.merge(
                            shared.ofType(SplashAction.FetchAccessTokenAction::class.java).compose(fetchAccessTokenProcessor),
                            shared.ofType(SplashAction.FetchLoginDataAction::class.java).compose(fetchLoginDataProcessor)
                    ).mergeWith(
                            shared.filter({ v ->
                                v !is SplashAction.FetchAccessTokenAction
                                        && v != SplashAction.FetchLoginDataAction
                            }).flatMap({ w ->
                                Observable.error<SplashResult>(
                                        IllegalArgumentException("Unknown Action type: $w"))
                            })
                    )
                }
            }
}