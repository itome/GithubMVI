package com.itome.githubmvi.ui.userdetail.core

import com.itome.githubmvi.mvibase.MviViewModel
import com.itome.githubmvi.ui.userdetail.core.UserDetailAction.*
import com.itome.githubmvi.ui.userdetail.core.UserDetailIntent.*
import com.itome.githubmvi.ui.userdetail.core.UserDetailResult.*
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class UserDetailViewModel @Inject constructor(
        private val actionProcessorHolder: UserDetailActionProcessorHolder
) : MviViewModel<UserDetailIntent, UserDetailViewState> {

    private val intentsSubject: PublishSubject<UserDetailIntent> = PublishSubject.create()
    private val statesObservable: Observable<UserDetailViewState> = compose()

    override fun processIntents(intents: Observable<UserDetailIntent>) {
        intents.subscribe(intentsSubject)
    }

    override fun states(): Observable<UserDetailViewState> = statesObservable

    private fun compose(): Observable<UserDetailViewState> {
        return intentsSubject
                .map(this::actionFromIntent)
                .compose(actionProcessorHolder.actionProcessor)
                .scan(UserDetailViewState.idle(), reducer)
                .replay(1)
                .autoConnect(0)
    }

    private fun actionFromIntent(intent: UserDetailIntent): UserDetailAction {
        return when (intent) {
            is FetchUserIntent -> FetchUserAction(intent.userName)
            is FetchUserReposIntent -> FetchUserReposAction(intent.userName)
            is CheckIsFollowedIntent -> CheckIsFollowedAction(intent.userName)
            FollowIntent -> FollowAction
            UnFollowIntent -> UnFollowAction
        }
    }

    companion object {
        private val reducer = { previousState: UserDetailViewState, result: UserDetailResult ->
            when (result) {
                is FetchUserResult -> when (result) {
                    is FetchUserResult.Success ->
                        previousState.copy(
                                user = result.user,
                                error = null,
                                isLoading = false
                        )
                    is FetchUserResult.Failure ->
                        previousState.copy(error = result.error, isLoading = false)
                    FetchUserResult.InFlight ->
                        previousState.copy(isLoading = true)
                }

                is FetchUserReposResult -> when (result) {
                    is FetchUserReposResult.Success ->
                        previousState.copy(
                                repos = result.repos,
                                error = null,
                                isLoading = false
                        )
                    is FetchUserReposResult.Feilure ->
                        previousState.copy(error = result.error, isLoading = false)
                    FetchUserReposResult.InFlight ->
                        previousState.copy(isLoading = true)
                }

                is CheckIsFollowedResult -> when (result) {
                    is CheckIsFollowedResult.Success ->
                        previousState.copy(
                                isFollowed = result.isFollowed,
                                error = null,
                                isLoading = false
                        )
                    is CheckIsFollowedResult.Failure ->
                        previousState.copy(error = result.error, isLoading = false)
                    CheckIsFollowedResult.InFlight ->
                        previousState.copy(isLoading = true)
                }

                is FollowResult -> when (result) {
                    FollowResult.Success -> previousState.copy(isFollowed = true)
                    is FollowResult.Failure -> previousState.copy(error = result.error)
                }

                is UnFollowResult -> when (result) {
                    UnFollowResult.Success -> previousState.copy(isFollowed = false)
                    is UnFollowResult.Failure -> previousState.copy(error = result.error)
                }
            }
        }
    }
}