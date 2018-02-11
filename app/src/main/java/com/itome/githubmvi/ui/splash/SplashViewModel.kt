package com.itome.githubmvi.ui.splash

import com.itome.githubmvi.mvibase.MviViewModel
import io.reactivex.Observable
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
                .map(this::actionFromIntent)
                .compose(actionProcessorHolder.actionProcessor)
                .scan(SplashViewState.idle(), reducer)
                .replay(1)
                .autoConnect(0)
    }

    private fun actionFromIntent(intent: SplashIntent): SplashAction {
        return when (intent) {
            SplashIntent.FetchLoginDataIntent ->
                SplashAction.FetchLoginDataAction
            is SplashIntent.FetchAccessTokenIntent ->
                SplashAction.FetchAccessTokenAction(intent.clientId, intent.clientSecret, intent.code)
        }
    }

    companion object {
        private val reducer = { previousState: SplashViewState, result: SplashResult ->
            when (result) {

                is SplashResult.FetchAccessTokenResult -> when (result) {
                    SplashResult.FetchAccessTokenResult.Success ->
                        previousState.copy(needsAccessToken = false, isLoading = false)
                    is SplashResult.FetchAccessTokenResult.Failure ->
                        previousState.copy(error = result.error, isLoading = false)
                    SplashResult.FetchAccessTokenResult.InFlight ->
                        previousState.copy(isLoading = true)
                }

                is SplashResult.FetchLoginDataResult -> when (result) {
                    is SplashResult.FetchLoginDataResult.Success ->
                        previousState.copy(user = result.user, isLoading = false)
                    is SplashResult.FetchLoginDataResult.Failure ->
                        previousState.copy(error = result.error, isLoading = false)
                    SplashResult.FetchLoginDataResult.NeedsAccessToken ->
                        previousState.copy(needsAccessToken = true)
                    SplashResult.FetchLoginDataResult.InFlight ->
                        previousState.copy(isLoading = true)
                }
            }
        }
    }
}