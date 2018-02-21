package com.itome.githubmvi

import com.itome.githubmvi.data.model.Repository
import com.itome.githubmvi.data.model.User
import com.itome.githubmvi.data.repository.LoginRepository
import com.itome.githubmvi.data.repository.UserRepository
import com.itome.githubmvi.scheduler.ImmediateSchedulerProvider
import com.itome.githubmvi.scheduler.SchedulerProvider
import com.itome.githubmvi.ui.userdetail.core.UserDetailActionProcessorHolder
import com.itome.githubmvi.ui.userdetail.core.UserDetailIntent
import com.itome.githubmvi.ui.userdetail.core.UserDetailViewModel
import com.itome.githubmvi.ui.userdetail.core.UserDetailViewState
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class UserDetailViewModelTest {

    companion object {
        private val expectedUser = User(id = 1, name = "testUser")
        private val expectedRepos = listOf(Repository(), Repository())
        private val isFollowed = false
        private val expectedError = Throwable("Test error occurred!!")
    }

    @Mock
    private lateinit var loginRepository: LoginRepository
    @Mock
    private lateinit var userRepository: UserRepository
    private lateinit var schedulerProvider: SchedulerProvider
    private lateinit var viewModel: UserDetailViewModel
    private lateinit var testObserver: TestObserver<UserDetailViewState>

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        schedulerProvider = ImmediateSchedulerProvider()

        viewModel = UserDetailViewModel(UserDetailActionProcessorHolder(loginRepository, userRepository, schedulerProvider))

        testObserver = viewModel.states().test()
    }

    @Test
    fun fetchUserIntent_Success() {
        val defaultState = UserDetailViewState.idle()
        whenever(userRepository.getUser(any())).thenReturn(Single.just(expectedUser))

        viewModel.processIntents(Observable.just(UserDetailIntent.FetchUserIntent(expectedUser.name)))

        verify(userRepository).getUser(expectedUser.name)
        testObserver.assertValueAt(1) {
            it == defaultState.copy(isLoading = true)
        }
        testObserver.assertValueAt(2) {
            it == defaultState.copy(user = expectedUser)
        }
    }

    @Test
    fun fetchUserIntent_Failure() {
        val defaultState = UserDetailViewState.idle()
        whenever(userRepository.getUser(any())).thenReturn(Single.error(expectedError))

        viewModel.processIntents(Observable.just(UserDetailIntent.FetchUserIntent(expectedUser.name)))

        verify(userRepository).getUser(expectedUser.name)
        testObserver.assertValueAt(1) {
            it == defaultState.copy(isLoading = true)
        }
        testObserver.assertValueAt(2) {
            it == defaultState.copy(error = expectedError)
        }
    }

    @Test
    fun fetchUserReposIntent_Success() {
        val defaultState = UserDetailViewState.idle()
        whenever(userRepository.getUserRepos(any())).thenReturn(Single.just(expectedRepos))

        viewModel.processIntents(Observable.just(UserDetailIntent.FetchUserReposIntent(expectedUser.name)))

        verify(userRepository).getUserRepos(expectedUser.name)
        testObserver.assertValueAt(1) {
            it == defaultState.copy(isLoading = true)
        }
        testObserver.assertValueAt(2) {
            it == defaultState.copy(repos = expectedRepos)
        }
    }

    @Test
    fun fetchUserReposIntent_Failure() {
        val defaultState = UserDetailViewState.idle()
        whenever(userRepository.getUserRepos(any())).thenReturn(Single.error(expectedError))

        viewModel.processIntents(Observable.just(UserDetailIntent.FetchUserReposIntent(expectedUser.name)))

        verify(userRepository).getUserRepos(expectedUser.name)
        testObserver.assertValueAt(1) {
            it == defaultState.copy(isLoading = true)
        }
        testObserver.assertValueAt(2) {
            it == defaultState.copy(error = expectedError)
        }
    }

    @Test
    fun checkIsLoginUserIntent_Success() {
        val defaultState = UserDetailViewState.idle()
        whenever(loginRepository.getLoginUser()).thenReturn(Single.just(expectedUser))

        viewModel.processIntents(Observable.just(UserDetailIntent.CheckIsLoginUserIntent(expectedUser.login)))

        verify(loginRepository).getLoginUser()
        testObserver.assertValueAt(1) {
            it == defaultState.copy(isLoading = true)
        }
        testObserver.assertValueAt(2) {
            it == defaultState.copy(isLoginUser = true)
        }
    }

    @Test
    fun checkIsLoginUserIntent_Failure() {
        val defaultState = UserDetailViewState.idle()
        whenever(loginRepository.getLoginUser()).thenReturn(Single.error(expectedError))

        viewModel.processIntents(Observable.just(UserDetailIntent.CheckIsLoginUserIntent(expectedUser.login)))

        verify(loginRepository).getLoginUser()
        testObserver.assertValueAt(1) {
            it == defaultState.copy(isLoading = true)
        }
        testObserver.assertValueAt(2) {
            it == defaultState.copy(error = expectedError)
        }
    }

    @Test
    fun checkIsFollowedIntent_Success() {
        val defaultState = UserDetailViewState.idle()
        whenever(userRepository.checkIsFollowed(any())).thenReturn(Single.just(isFollowed))

        viewModel.processIntents(Observable.just(UserDetailIntent.CheckIsFollowedIntent(expectedUser.name)))

        verify(userRepository).checkIsFollowed(expectedUser.name)
        testObserver.assertValueAt(1) {
            it == defaultState.copy(isLoading = true)
        }
        testObserver.assertValueAt(2) {
            it == defaultState.copy(isFollowed = isFollowed)
        }
    }

    @Test
    fun checkIsFollowedIntent_Failure() {
        val defaultState = UserDetailViewState.idle()
        whenever(userRepository.checkIsFollowed(any())).thenReturn(Single.error(expectedError))

        viewModel.processIntents(Observable.just(UserDetailIntent.CheckIsFollowedIntent(expectedUser.name)))

        verify(userRepository).checkIsFollowed(expectedUser.name)
        testObserver.assertValueAt(1) {
            it == defaultState.copy(isLoading = true)
        }
        testObserver.assertValueAt(2) {
            it == defaultState.copy(error = expectedError)
        }
    }

    @Test
    fun followIntent_Success() {
        val defaultState = UserDetailViewState.idle()
        whenever(userRepository.followUser(any())).thenReturn(Completable.complete())

        viewModel.processIntents(Observable.just(UserDetailIntent.FollowIntent(expectedUser.name)))

        verify(userRepository).followUser(expectedUser.name)
        testObserver.assertValueAt(1) {
            it == defaultState.copy(
                    isFollowed = true,
                    user = defaultState.user?.plusFollowerCount()
            )
        }
    }

    @Test
    fun followIntent_Failure() {
        val defaultState = UserDetailViewState.idle()
        whenever(userRepository.followUser(any())).thenReturn(Completable.error(expectedError))

        viewModel.processIntents(Observable.just(UserDetailIntent.FollowIntent(expectedUser.name)))

        verify(userRepository).followUser(expectedUser.name)
        testObserver.assertValueAt(1) {
            it == defaultState.copy(error = expectedError)
        }
    }

    @Test
    fun unFollowIntent_Success() {
        val defaultState = UserDetailViewState.idle()
        whenever(userRepository.unFollowUser(any())).thenReturn(Completable.complete())

        viewModel.processIntents(Observable.just(UserDetailIntent.UnFollowIntent(expectedUser.name)))

        verify(userRepository).unFollowUser(expectedUser.name)
        testObserver.assertValueAt(1) {
            it == defaultState.copy(
                    isFollowed = false,
                    user = defaultState.user?.minusFollowerCount()
            )
        }
    }

    @Test
    fun unFollowIntent_Failure() {
        val defaultState = UserDetailViewState.idle()
        whenever(userRepository.unFollowUser(any())).thenReturn(Completable.error(expectedError))

        viewModel.processIntents(Observable.just(UserDetailIntent.UnFollowIntent(expectedUser.name)))

        verify(userRepository).unFollowUser(expectedUser.name)
        testObserver.assertValueAt(1) {
            it == defaultState.copy(error = expectedError)
        }
    }
}