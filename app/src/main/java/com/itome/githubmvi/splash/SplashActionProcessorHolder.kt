package com.itome.githubmvi.splash

import com.itome.githubmvi.splash.SplashResult.*
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SplashActionProcessorHolder {

    private val doNothingProcessor =
            ObservableTransformer<SplashAction.DoNothingAction, DoNothingResult> { actions ->
                actions.flatMap {
                    Observable.just(DoNothingResult.Success)
                            .cast(DoNothingResult::class.java)
                            .onErrorReturn(DoNothingResult::Failure)
                }
            }

    private val fetchAccessTokenProcessor =
            ObservableTransformer<SplashAction.FetchAccessTokenAction, FetchAccessTokenResult> { actions ->
                actions.flatMap { action ->
                    Observable.just(FetchAccessTokenResult.InFlight)
                            .cast(FetchAccessTokenResult::class.java)
                            .onErrorReturn(FetchAccessTokenResult::Failure)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .startWith(FetchAccessTokenResult.InFlight)
                }
            }

    private val fetchSelfDataProcessor =
            ObservableTransformer<SplashAction.FetchSelfDataAction, SplashResult.FetchSelfDataResult> { actions ->
                actions.flatMap { action ->
                    Observable.just(FetchSelfDataResult.InFlight)
                            .cast(FetchSelfDataResult::class.java)
                            .onErrorReturn(FetchSelfDataResult::Failure)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .startWith(FetchSelfDataResult.InFlight)
                }
            }

    internal var actionProcessor =
            ObservableTransformer<SplashAction, SplashResult> { actions ->
                actions.publish { shared ->
                    Observable.merge(
                            shared.ofType(SplashAction.DoNothingAction::class.java).compose(doNothingProcessor),
                            shared.ofType(SplashAction.FetchAccessTokenAction::class.java).compose(fetchAccessTokenProcessor),
                            shared.ofType(SplashAction.FetchSelfDataAction::class.java).compose(fetchSelfDataProcessor)
                    ).mergeWith(
                            shared.filter({ v ->
                                v != SplashAction.DoNothingAction
                                        && v !is SplashAction.FetchAccessTokenAction
                                        && v !is SplashAction.FetchSelfDataAction
                            }).flatMap({ w ->
                                Observable.error<SplashResult>(
                                        IllegalArgumentException("Unknown Action type: $w"))
                            })
                    )
                }
            }
}