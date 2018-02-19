package com.itome.githubmvi.ui.login.core

import com.itome.githubmvi.extensions.notOfType
import com.itome.githubmvi.mvibase.MviViewModel
import com.itome.githubmvi.ui.login.core.LoginAction.FetchAccessTokenAction
import com.itome.githubmvi.ui.login.core.LoginAction.FetchLoginDataAction
import com.itome.githubmvi.ui.login.core.LoginIntent.FetchAccessTokenIntent
import com.itome.githubmvi.ui.login.core.LoginIntent.FetchLoginDataIntent
import com.itome.githubmvi.ui.login.core.LoginResult.FetchAccessTokenResult
import com.itome.githubmvi.ui.login.core.LoginResult.FetchLoginDataResult
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class LoginViewModel @Inject constructor(
        private val actionProcessorHolder: LoginActionProcessorHolder
) : MviViewModel<LoginIntent, LoginViewState> {

    private val intentsSubject: PublishSubject<LoginIntent> = PublishSubject.create()
    private val statesObservable: Observable<LoginViewState> = compose()

    override fun processIntents(intents: Observable<LoginIntent>) {
        intents.subscribe(intentsSubject)
    }

    override fun states(): Observable<LoginViewState> = statesObservable

    private fun compose(): Observable<LoginViewState> {
        return intentsSubject
                .compose(intentFilter)
                .map(this::actionFromIntent)
                .compose(actionProcessorHolder.actionProcessor)
                .scan(LoginViewState.idle(), reducer)
                .replay(1)
                .autoConnect(0)
    }

    private val intentFilter: ObservableTransformer<LoginIntent, LoginIntent>
        get() = ObservableTransformer { intents ->
            intents.publish { shared ->
                Observable.merge(
                        shared.ofType(FetchAccessTokenIntent::class.java).take(1),
                        shared.notOfType(FetchAccessTokenIntent::class.java)
                )
            }
        }

    private fun actionFromIntent(intent: LoginIntent): LoginAction {
        return when (intent) {
            FetchLoginDataIntent -> FetchLoginDataAction
            is FetchAccessTokenIntent ->
                FetchAccessTokenAction(intent.clientId, intent.clientSecret, intent.code)
        }
    }

    companion object {
        private val reducer = { previousState: LoginViewState, result: LoginResult ->
            when (result) {

                is FetchAccessTokenResult -> when (result) {
                    is FetchAccessTokenResult.Success ->
                        previousState.copy(
                                userName = result.userName,
                                userImageUrl = result.userImageUrl,
                                needsAccessToken = false,
                                isLoading = false
                        )
                    is FetchAccessTokenResult.Failure ->
                        previousState.copy(
                                error = result.error,
                                needsAccessToken = true,
                                isLoading = false
                        )
                    FetchAccessTokenResult.StartNextActivity ->
                        previousState.copy(
                                needsAccessToken = false,
                                startNextActivity = true,
                                isLoading = false
                        )
                    FetchAccessTokenResult.InFlight ->
                        previousState.copy(
                                isLoading = true,
                                needsAccessToken = false
                        )
                }

                is FetchLoginDataResult -> when (result) {
                    is FetchLoginDataResult.Success ->
                        previousState.copy(
                                userName = result.userName,
                                userImageUrl = result.userImageUrl,
                                isLoading = false)
                    is FetchLoginDataResult.Failure ->
                        previousState.copy(
                                error = result.error,
                                isLoading = false
                        )
                    FetchLoginDataResult.NeedsAccessToken ->
                        previousState.copy(needsAccessToken = true, isLoading = false)
                    FetchLoginDataResult.StartNextActivity ->
                        previousState.copy(startNextActivity = true)
                    FetchLoginDataResult.InFlight ->
                        previousState.copy(isLoading = true)
                }
            }
        }
    }
}