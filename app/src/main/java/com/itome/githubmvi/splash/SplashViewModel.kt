package com.itome.githubmvi.splash

import com.itome.githubmvi.extensions.notOfType
import com.itome.githubmvi.mvibase.MviViewModel
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject

class SplashViewModel(
        private val actionProcessorHolder: SplashActionProcessorHolder
) : MviViewModel<SplashIntent, SplashViewState> {

    private val intentsSubject: PublishSubject<SplashIntent> = PublishSubject.create()
    private val statesObservable: Observable<SplashViewState> = compose()

    override fun processIntents(intents: Observable<SplashIntent>) {
        intents.subscribe(intentsSubject)
    }

    override fun states(): Observable<SplashViewState> = statesObservable

    private fun compose(): Observable<SplashViewState> {
        return intentsSubject
                .compose(intentFilter)
                .map(this::actionFromIntent)
                .compose(actionProcessorHolder.actionProcessor)
                .scan(SplashViewState.idle(), reducer)
                .replay(1)
                .autoConnect(0)
    }

    private val intentFilter: ObservableTransformer<SplashIntent, SplashIntent>
        get() = ObservableTransformer { intents ->
            intents.publish { shared ->
                Observable.merge(
                        shared.ofType(SplashIntent.InitialIntent::class.java).take(1),
                        shared.notOfType(SplashIntent.InitialIntent::class.java)
                )
            }
        }

    private fun actionFromIntent(intent: SplashIntent): SplashAction {
        return when (intent) {
            SplashIntent.InitialIntent ->
                SplashAction.DoNothingAction
            is SplashIntent.FetchSelfDataIntent ->
                SplashAction.FetchSelfDataAction(intent.accessToken)
            is SplashIntent.FetchAccessTokenIntent ->
                SplashAction.FetchAccessTokenAction(intent.clientId, intent.clientSecret, intent.code)
        }
    }

    companion object {
        private val reducer = BiFunction { previousState: SplashViewState, result: SplashResult ->
            when (result) {
                is SplashResult.DoNothingResult -> when(result) {
                    SplashResult.DoNothingResult.Success -> previousState
                    is SplashResult.DoNothingResult.Failure -> previousState.copy(error = result.error)
                }

                is SplashResult.FetchAccessTokenResult -> when (result) {
                    is SplashResult.FetchAccessTokenResult.Success ->
                        previousState.copy(accessToken = result.accessToken, isLoading = false)
                    is SplashResult.FetchAccessTokenResult.Failure ->
                        previousState.copy(error = result.error, isLoading = false)
                    SplashResult.FetchAccessTokenResult.InFlight ->
                        previousState.copy(isLoading = true)
                }

                is SplashResult.FetchSelfDataResult -> when (result) {
                    is SplashResult.FetchSelfDataResult.Success ->
                        previousState.copy(user = result.user, isLoading = false)
                    is SplashResult.FetchSelfDataResult.Failure ->
                        previousState.copy(error = result.error, isLoading = false)
                    SplashResult.FetchSelfDataResult.InFlight ->
                        previousState.copy(isLoading = true)
                }
            }
        }
    }
}