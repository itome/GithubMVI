package com.itome.githubmvi.ui.repository.core

import com.itome.githubmvi.mvibase.MviViewModel
import com.itome.githubmvi.ui.repository.core.RepositoryAction.*
import com.itome.githubmvi.ui.repository.core.RepositoryIntent.*
import com.itome.githubmvi.ui.repository.core.RepositoryResult.*
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class RepositoryViewModel @Inject constructor(
        private val actionProcessorHolder: RepositoryActionProcessorHolder
) : MviViewModel<RepositoryIntent, RepositoryViewState> {

    private val intentsSubject = PublishSubject.create<RepositoryIntent>()
    private val statesObservable = compose()

    override fun states(): Observable<RepositoryViewState> = statesObservable

    override fun processIntents(intents: Observable<RepositoryIntent>) {
        intents.subscribe(intentsSubject)
    }

    private fun compose(): Observable<RepositoryViewState> {
        return intentsSubject
                .map(this::actionFromIntent)
                .compose(actionProcessorHolder.actionProcessor)
                .scan(RepositoryViewState.idle(), reducer)
                .replay(1)
                .autoConnect(0)
    }

    private fun actionFromIntent(intent: RepositoryIntent): RepositoryAction {
        return when (intent) {
            is FetchRepositoryIntent -> FetchRepositoryAction(intent.repoName)
            is FetchReadmeIntent -> FetchReadmeAction(intent.repoName)
            is CheckIsStarredIntent -> CheckIsStarredAction(intent.repoName)
            is StarIntent -> StarAction(intent.repoName)
            is UnStarIntent -> UnStarAction(intent.repoName)
        }
    }

    companion object {
        private val reducer = { previousState: RepositoryViewState, result: RepositoryResult ->
            when (result) {
                is FetchRepositoryResult -> when (result) {
                    is FetchRepositoryResult.Success ->
                        previousState.copy(
                                repository = result.repository,
                                error = null,
                                isLoading = false
                        )
                    is FetchRepositoryResult.Failure ->
                        previousState.copy(error = result.error, isLoading = false)
                    FetchRepositoryResult.InFlight ->
                        previousState.copy(isLoading = true)
                }
                is FetchReadmeResult -> when (result) {
                    is FetchReadmeResult.Success ->
                        previousState.copy(
                                readme = result.readme,
                                error = null,
                                isLoading = false
                        )
                    is FetchReadmeResult.Failure ->
                        previousState.copy(error = result.error, isLoading = false)
                    FetchReadmeResult.InFlight ->
                        previousState.copy(isLoading = true)
                }
                is CheckIsStarredResult -> when (result) {
                    is CheckIsStarredResult.Success ->
                        previousState.copy(
                                isStarred = result.isStarred,
                                error = null,
                                isLoading = false
                        )
                    is CheckIsStarredResult.Failure ->
                        previousState.copy(error = result.error, isLoading = false)
                    CheckIsStarredResult.InFlight ->
                        previousState.copy(isLoading = true)
                }
                is StarResult -> when (result) {
                    StarResult.Success ->
                        previousState.copy(
                                isStarred = true,
                                repository = previousState.repository?.plusStarCount()
                        )
                    is StarResult.Failure -> previousState.copy(error = result.error)
                }
                is UnStarResult -> when (result) {
                    UnStarResult.Success ->
                        previousState.copy(
                                isStarred = false,
                                repository = previousState.repository?.minusStarCount()
                        )
                    is UnStarResult.Failure -> previousState.copy(error = result.error)
                }
            }
        }
    }
}