package com.itome.githubmvi.ui.userdetail.core

import com.itome.githubmvi.data.repository.LoginRepository
import com.itome.githubmvi.data.repository.UserRepository
import com.itome.githubmvi.mvibase.MviProcessorHolder
import com.itome.githubmvi.scheduler.SchedulerProvider
import com.itome.githubmvi.ui.userdetail.core.UserDetailAction.*
import com.itome.githubmvi.ui.userdetail.core.UserDetailResult.*
import io.reactivex.Observable
import javax.inject.Inject

class UserDetailProcessorHolder @Inject constructor(
    private val loginRepository: LoginRepository,
    private val repository: UserRepository,
    private val schedulerProvider: SchedulerProvider
) : MviProcessorHolder<UserDetailAction, UserDetailResult>() {

    private val fetchUserProcessor =
        createProcessor<FetchUserAction, FetchUserResult> { action ->
            repository.getUser(action.userName)
                .toObservable()
                .map { user -> FetchUserResult.Success(user) }
                .cast(FetchUserResult::class.java)
                .onErrorReturn(FetchUserResult::Failure)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .startWith(FetchUserResult.InFlight)
        }

    private val fetchUserReposProcessor =
        createProcessor<FetchUserReposAction, FetchUserReposResult> { action ->
            repository.getUserRepos(action.userName)
                .toObservable()
                .map { repos -> FetchUserReposResult.Success(repos) }
                .cast(FetchUserReposResult::class.java)
                .onErrorReturn(FetchUserReposResult::Failure)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .startWith(FetchUserReposResult.InFlight)
        }

    private val checkIsLoginUserProcessor =
        createProcessor<CheckIsLoginUserAction, CheckIsLoginUserResult> { action ->
            loginRepository.getLoginUser()
                .toObservable()
                .map { user -> CheckIsLoginUserResult.Success(user.login == action.userName) }
                .cast(CheckIsLoginUserResult::class.java)
                .onErrorReturn(CheckIsLoginUserResult::Failure)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .startWith(CheckIsLoginUserResult.InFlight)
        }

    private val checkIsFollowedProcessor =
        createProcessor<CheckIsFollowedAction, CheckIsFollowedResult> { action ->
            repository.checkIsFollowed(action.userName)
                .toObservable()
                .map { isFollowed -> CheckIsFollowedResult.Success(isFollowed) }
                .cast(CheckIsFollowedResult::class.java)
                .onErrorReturn(CheckIsFollowedResult::Failure)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .startWith(CheckIsFollowedResult.InFlight)
        }

    private val followUserProcessor =
        createProcessor<FollowAction, FollowResult> { action ->
            repository.followUser(action.userName)
                .andThen(Observable.just(FollowResult.Success))
                .cast(FollowResult::class.java)
                .onErrorReturn(FollowResult::Failure)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
        }

    private val unFollowUserProcessor =
        createProcessor<UnFollowAction, UnFollowResult> { action ->
            repository.unFollowUser(action.userName)
                .andThen(Observable.just(UnFollowResult.Success))
                .cast(UnFollowResult::class.java)
                .onErrorReturn(UnFollowResult::Failure)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
        }

    override val actionProcessor = mergeProcessor(
        fetchUserProcessor to FetchUserAction::class,
        fetchUserReposProcessor to FetchUserReposAction::class,
        checkIsLoginUserProcessor to CheckIsLoginUserAction::class,
        checkIsFollowedProcessor to CheckIsFollowedAction::class,
        followUserProcessor to FollowAction::class,
        unFollowUserProcessor to UnFollowAction::class
    )
}