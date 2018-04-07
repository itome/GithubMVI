package com.itome.githubmvi.ui.repository.core

import com.itome.githubmvi.data.repository.ReposRepository
import com.itome.githubmvi.scheduler.SchedulerProvider
import com.itome.githubmvi.ui.repository.core.RepositoryAction.*
import com.itome.githubmvi.ui.repository.core.RepositoryResult.*
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import javax.inject.Inject

class RepositoryProcessorHolder @Inject constructor(
    private val repository: ReposRepository,
    private val schedulerProvider: SchedulerProvider
) {
    private val fetchRepositoryProcessor =
        ObservableTransformer<FetchRepositoryAction, FetchRepositoryResult> { actions ->
            actions.flatMap { action ->
                repository.getRepository(action.repoName)
                    .toObservable()
                    .map { repository -> FetchRepositoryResult.Success(repository) }
                    .cast(FetchRepositoryResult::class.java)
                    .onErrorReturn(FetchRepositoryResult::Failure)
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .startWith(FetchRepositoryResult.InFlight)
            }
        }

    private val fetchReadmeProcessor =
        ObservableTransformer<FetchReadmeAction, FetchReadmeResult> { actions ->
            actions.flatMap { action ->
                repository.getReadme(action.repoName)
                    .toObservable()
                    .map { readme -> FetchReadmeResult.Success(readme) }
                    .cast(FetchReadmeResult::class.java)
                    .onErrorReturn(FetchReadmeResult::Failure)
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .startWith(FetchReadmeResult.InFlight)
            }
        }

    private val checkIsStarredProcessor =
        ObservableTransformer<CheckIsStarredAction, CheckIsStarredResult> { actions ->
            actions.flatMap { action ->
                repository.checkIsStarred(action.repoName)
                    .toObservable()
                    .map { isStarred -> CheckIsStarredResult.Success(isStarred) }
                    .cast(CheckIsStarredResult::class.java)
                    .onErrorReturn(CheckIsStarredResult::Failure)
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .startWith(CheckIsStarredResult.InFlight)
            }
        }

    private val starRepositoryProcessor =
        ObservableTransformer<StarAction, StarResult> { actions ->
            actions.flatMap { action ->
                repository.starRepository(action.repoName)
                    .andThen(Observable.just(StarResult.Success))
                    .cast(StarResult::class.java)
                    .onErrorReturn(StarResult::Failure)
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
            }
        }

    private val unStarRepositoryProcessor =
        ObservableTransformer<UnStarAction, UnStarResult> { actions ->
            actions.flatMap { action ->
                repository.unStarRepository(action.repoName)
                    .andThen(Observable.just(UnStarResult.Success))
                    .cast(UnStarResult::class.java)
                    .onErrorReturn(UnStarResult::Failure)
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
            }
        }

    internal var actionProcessor =
        ObservableTransformer<RepositoryAction, RepositoryResult> { actions ->
            actions.publish { shared ->
                Observable.merge(
                    listOf(
                        shared.ofType(FetchRepositoryAction::class.java).compose(
                            fetchRepositoryProcessor
                        ),
                        shared.ofType(FetchReadmeAction::class.java).compose(fetchReadmeProcessor),
                        shared.ofType(CheckIsStarredAction::class.java).compose(
                            checkIsStarredProcessor
                        ),
                        shared.ofType(StarAction::class.java).compose(starRepositoryProcessor),
                        shared.ofType(UnStarAction::class.java).compose(unStarRepositoryProcessor)
                    )
                ).mergeWith(
                    shared.filter({ v ->
                        v !is FetchRepositoryAction &&
                                v !is FetchReadmeAction &&
                                v !is CheckIsStarredAction &&
                                v !is StarAction &&
                                v !is UnStarAction
                    }).flatMap({ w ->
                        Observable.error<RepositoryResult>(
                            IllegalArgumentException("Unknown Action type: $w")
                        )
                    })
                )
            }
        }
}