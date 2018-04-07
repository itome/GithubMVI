package com.itome.githubmvi.ui.repository.core

import com.itome.githubmvi.data.repository.ReposRepository
import com.itome.githubmvi.mvibase.MviProcessorHolder
import com.itome.githubmvi.scheduler.SchedulerProvider
import com.itome.githubmvi.ui.repository.core.RepositoryAction.*
import com.itome.githubmvi.ui.repository.core.RepositoryResult.*
import io.reactivex.Observable
import javax.inject.Inject

class RepositoryProcessorHolder @Inject constructor(
    private val repository: ReposRepository,
    private val schedulerProvider: SchedulerProvider
) : MviProcessorHolder<RepositoryAction, RepositoryResult>() {

    private val fetchRepositoryProcessor =
        createProcessor<FetchRepositoryAction, FetchRepositoryResult> { action ->
            repository.getRepository(action.repoName)
                .toObservable()
                .map { repository -> FetchRepositoryResult.Success(repository) }
                .cast(FetchRepositoryResult::class.java)
                .onErrorReturn(FetchRepositoryResult::Failure)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .startWith(FetchRepositoryResult.InFlight)
        }

    private val fetchReadmeProcessor =
        createProcessor<FetchReadmeAction, FetchReadmeResult> { action ->
            repository.getReadme(action.repoName)
                .toObservable()
                .map { readme -> FetchReadmeResult.Success(readme) }
                .cast(FetchReadmeResult::class.java)
                .onErrorReturn(FetchReadmeResult::Failure)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .startWith(FetchReadmeResult.InFlight)
        }

    private val checkIsStarredProcessor =
        createProcessor<CheckIsStarredAction, CheckIsStarredResult> { action ->
            repository.checkIsStarred(action.repoName)
                .toObservable()
                .map { isStarred -> CheckIsStarredResult.Success(isStarred) }
                .cast(CheckIsStarredResult::class.java)
                .onErrorReturn(CheckIsStarredResult::Failure)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .startWith(CheckIsStarredResult.InFlight)
        }

    private val starRepositoryProcessor =
        createProcessor<StarAction, StarResult> { action ->
            repository.starRepository(action.repoName)
                .andThen(Observable.just(StarResult.Success))
                .cast(StarResult::class.java)
                .onErrorReturn(StarResult::Failure)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
        }

    private val unStarRepositoryProcessor =
        createProcessor<UnStarAction, UnStarResult> { action ->
            repository.unStarRepository(action.repoName)
                .andThen(Observable.just(UnStarResult.Success))
                .cast(UnStarResult::class.java)
                .onErrorReturn(UnStarResult::Failure)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
        }

    override val actionProcessor = mergeProcessor(
        fetchRepositoryProcessor to FetchRepositoryAction::class,
        fetchReadmeProcessor to FetchReadmeAction::class,
        checkIsStarredProcessor to CheckIsStarredAction::class,
        starRepositoryProcessor to StarAction::class,
        unStarRepositoryProcessor to UnStarAction::class
    )
}