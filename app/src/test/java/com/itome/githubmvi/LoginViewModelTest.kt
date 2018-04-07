package com.itome.githubmvi

import com.itome.githubmvi.data.model.User
import com.itome.githubmvi.data.repository.LoginRepository
import com.itome.githubmvi.scheduler.ImmediateSchedulerProvider
import com.itome.githubmvi.scheduler.SchedulerProvider
import com.itome.githubmvi.ui.login.core.LoginProcessorHolder
import com.itome.githubmvi.ui.login.core.LoginIntent
import com.itome.githubmvi.ui.login.core.LoginViewModel
import com.itome.githubmvi.ui.login.core.LoginViewState
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class LoginViewModelTest {

    companion object {
        private val expectedUser = User(id = 1, name = "testUser")
        private val expectedError = Throwable("Test error occurred!!")
    }

    @Mock
    private lateinit var loginRepository: LoginRepository
    private lateinit var schedulerProvider: SchedulerProvider
    private lateinit var viewModel: LoginViewModel
    private lateinit var testObserver: TestObserver<LoginViewState>

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        schedulerProvider = ImmediateSchedulerProvider()

        viewModel = LoginViewModel(LoginProcessorHolder(loginRepository, schedulerProvider))

        testObserver = viewModel.states().test()
    }

    @Test
    fun fetchAccessTokenIntent_Success() {
        val defaultState = LoginViewState.idle()
        val clientId = "clientId"
        val clientSecret = "clientSecret"
        val code = "code"

        whenever(loginRepository.fetchAccessToken(any(), any(), any())).thenReturn(Single.just("token"))
        whenever(loginRepository.fetchLoginUser()).thenReturn(Single.just(expectedUser))

        viewModel.processIntents(Observable.just(LoginIntent.FetchAccessTokenIntent(
                clientId, clientSecret, code)))

        verify(loginRepository).fetchAccessToken(clientId, clientSecret, code)
        verify(loginRepository).fetchLoginUser()
        testObserver.assertValueAt(1) {
            it == defaultState.copy(isLoading = true)
        }
        testObserver.assertValueAt(2) {
            it == defaultState.copy(
                    userName = expectedUser.name,
                    userImageUrl = expectedUser.avatar_url
            )
        }
    }

    @Test
    fun fetchAccessTokenIntent_Failure() {
        val defaultState = LoginViewState.idle()
        val clientId = "clientId"
        val clientSecret = "clientSecret"
        val code = "code"

        whenever(loginRepository.fetchAccessToken(any(), any(), any())).thenReturn(Single.error(expectedError))
        whenever(loginRepository.fetchLoginUser()).thenReturn(Single.just(expectedUser))

        viewModel.processIntents(Observable.just(LoginIntent.FetchAccessTokenIntent(
                clientId, clientSecret, code)))

        verify(loginRepository).fetchAccessToken(clientId, clientSecret, code)
        testObserver.assertValueAt(1) {
            it == defaultState.copy(isLoading = true)
        }
        testObserver.assertValueAt(2) {
            it == defaultState.copy(error = expectedError, needsAccessToken = true)
        }
    }

    @Test
    fun fetchLoginDataIntent_Success() {
        val defaultState = LoginViewState.idle()

        whenever(loginRepository.readAccessToken()).thenReturn(Single.just("token"))
        whenever(loginRepository.getLoginUser()).thenReturn(Single.just(expectedUser))

        viewModel.processIntents(Observable.just(LoginIntent.FetchLoginDataIntent))

        verify(loginRepository).readAccessToken()
        verify(loginRepository).getLoginUser()
        testObserver.assertValueAt(1) {
            it == defaultState.copy(isLoading = true)
        }
        testObserver.assertValueAt(2) {
            it == defaultState.copy(
                    userName = expectedUser.name,
                    userImageUrl = expectedUser.avatar_url
            )
        }
    }

    @Test
    fun fetchLoginDataIntent_NeedsAccessToken() {
        val defaultState = LoginViewState.idle()
        whenever(loginRepository.readAccessToken()).thenReturn(Single.just(""))

        viewModel.processIntents(Observable.just(LoginIntent.FetchLoginDataIntent))

        verify(loginRepository).readAccessToken()
        testObserver.assertValueAt(1) {
            it == defaultState.copy(isLoading = true)
        }
        testObserver.assertValueAt(2) {
            it == defaultState.copy(needsAccessToken = true)
        }
    }

    @Test
    fun fetchLoginDataIntent_Failure() {
        val defaultState = LoginViewState.idle()
        whenever(loginRepository.readAccessToken()).thenReturn(Single.error(expectedError))

        viewModel.processIntents(Observable.just(LoginIntent.FetchLoginDataIntent))

        verify(loginRepository).readAccessToken()
        testObserver.assertValueAt(1) {
            it == defaultState.copy(isLoading = true)
        }
        testObserver.assertValueAt(2) {
            it == defaultState.copy(error = expectedError)
        }
    }
}