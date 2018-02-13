package com.itome.githubmvi.ui.login

import com.itome.githubmvi.extensions.notOfType
import com.itome.githubmvi.mvibase.MviViewModel
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.subjects.PublishSubject

class LoginViewModel(
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
                        shared.ofType(LoginIntent.FetchAccessTokenIntent::class.java).take(1),
                        shared.notOfType(LoginIntent.FetchAccessTokenIntent::class.java)
                )
            }
        }

    private fun actionFromIntent(intent: LoginIntent): LoginAction {
        return when (intent) {
            LoginIntent.FetchLoginDataIntent ->
                LoginAction.FetchLoginDataAction
            is LoginIntent.FetchAccessTokenIntent ->
                LoginAction.FetchAccessTokenAction(intent.clientId, intent.clientSecret, intent.code)
        }
    }

    companion object {
        private val reducer = { previousState: LoginViewState, result: LoginResult ->
            when (result) {

                is LoginResult.FetchAccessTokenResult -> when (result) {
                    is LoginResult.FetchAccessTokenResult.Success ->
                        previousState.copy(
                                userName = result.userName,
                                userImageUrl = result.userImageUrl,
                                needsAccessToken = false,
                                isLoading = false
                        )
                    is LoginResult.FetchAccessTokenResult.Failure ->
                        previousState.copy(
                                error = result.error,
                                needsAccessToken = true,
                                isLoading = false
                        )
                    LoginResult.FetchAccessTokenResult.StartNextActivity ->
                        previousState.copy(
                                needsAccessToken = false,
                                startNextActivity = true,
                                isLoading = false
                        )
                    LoginResult.FetchAccessTokenResult.InFlight ->
                        previousState.copy(
                                isLoading = true,
                                needsAccessToken = false
                        )
                }

                is LoginResult.FetchLoginDataResult -> when (result) {
                    is LoginResult.FetchLoginDataResult.Success ->
                        previousState.copy(
                                userName = result.userName,
                                userImageUrl = result.userImageUrl,
                                isLoading = false)
                    is LoginResult.FetchLoginDataResult.Failure ->
                        previousState.copy(
                                error = result.error,
                                isLoading = false
                        )
                    LoginResult.FetchLoginDataResult.NeedsAccessToken ->
                        previousState.copy(needsAccessToken = true)
                    LoginResult.FetchLoginDataResult.StartNextActivity ->
                        previousState.copy(startNextActivity = true)
                    LoginResult.FetchLoginDataResult.InFlight ->
                        previousState.copy(isLoading = true)
                }
            }
        }
    }
}