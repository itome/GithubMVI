package com.itome.githubmvi

import com.itome.githubmvi.data.model.Readme
import com.itome.githubmvi.data.model.Repository
import com.itome.githubmvi.data.repository.ReposRepository
import com.itome.githubmvi.scheduler.ImmediateSchedulerProvider
import com.itome.githubmvi.scheduler.SchedulerProvider
import com.itome.githubmvi.ui.repository.core.RepositoryActionProcessorHolder
import com.itome.githubmvi.ui.repository.core.RepositoryIntent
import com.itome.githubmvi.ui.repository.core.RepositoryViewModel
import com.itome.githubmvi.ui.repository.core.RepositoryViewState
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

class RepositoryViewModelTest {

    companion object {
        private val expectedRepository = Repository(id = 1, name = "testRepo")
        private val expectedReadme = Readme(type = "test")
        private val isStarred = false
        private val expectedError = Throwable("Test error occurred!!")
    }

    @Mock
    private lateinit var reposRepository: ReposRepository
    private lateinit var schedulerProvider: SchedulerProvider
    private lateinit var viewModel: RepositoryViewModel
    private lateinit var testObserver: TestObserver<RepositoryViewState>

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        schedulerProvider = ImmediateSchedulerProvider()

        viewModel = RepositoryViewModel(RepositoryActionProcessorHolder(reposRepository, schedulerProvider))

        testObserver = viewModel.states().test()
    }

    @Test
    fun fetchRepositoryIntent_Success() {
        val defaultState = RepositoryViewState.idle()
        whenever(reposRepository.getRepository(any())).thenReturn(Single.just(expectedRepository))

        viewModel.processIntents(Observable.just(RepositoryIntent.FetchRepositoryIntent(expectedRepository.full_name)))

        verify(reposRepository).getRepository(expectedRepository.full_name)
        testObserver.assertValueAt(1) {
            it == defaultState.copy(isLoading = true)
        }
        testObserver.assertValueAt(2) {
            it == defaultState.copy(repository = expectedRepository)
        }
    }

    @Test
    fun fetchRepositoryIntent_Failure() {
        val defaultState = RepositoryViewState.idle()
        whenever(reposRepository.getRepository(any())).thenReturn(Single.error(expectedError))

        viewModel.processIntents(Observable.just(RepositoryIntent.FetchRepositoryIntent(expectedRepository.full_name)))

        verify(reposRepository).getRepository(expectedRepository.full_name)
        testObserver.assertValueAt(1) {
            it == defaultState.copy(isLoading = true)
        }
        testObserver.assertValueAt(2) {
            it == defaultState.copy(error = expectedError)
        }
    }

    @Test
    fun fetchReadmeIntent_Success() {
        val defaultState = RepositoryViewState.idle()
        whenever(reposRepository.getReadme(any())).thenReturn(Single.just(expectedReadme))

        viewModel.processIntents(Observable.just(RepositoryIntent.FetchReadmeIntent(expectedRepository.full_name)))

        verify(reposRepository).getReadme(expectedRepository.full_name)
        testObserver.assertValueAt(1) {
            it == defaultState.copy(isLoading = true)
        }
        testObserver.assertValueAt(2) {
            it == defaultState.copy(readme = expectedReadme)
        }
    }

    @Test
    fun fetchReadmeIntent_Failure() {
        val defaultState = RepositoryViewState.idle()
        whenever(reposRepository.getReadme(any())).thenReturn(Single.error(expectedError))

        viewModel.processIntents(Observable.just(RepositoryIntent.FetchReadmeIntent(expectedRepository.full_name)))

        verify(reposRepository).getReadme(expectedRepository.full_name)
        testObserver.assertValueAt(1) {
            it == defaultState.copy(isLoading = true)
        }
        testObserver.assertValueAt(2) {
            it == defaultState.copy(error = expectedError)
        }
    }

    @Test
    fun checkIsStarredIntent_Success() {
        val defaultState = RepositoryViewState.idle()
        whenever(reposRepository.checkIsStarred(any())).thenReturn(Single.just(isStarred))

        viewModel.processIntents(Observable.just(RepositoryIntent.CheckIsStarredIntent(expectedRepository.full_name)))

        verify(reposRepository).checkIsStarred(expectedRepository.full_name)
        testObserver.assertValueAt(1) {
            it == defaultState.copy(isLoading = true)
        }
        testObserver.assertValueAt(2) {
            it == defaultState.copy(isStarred = isStarred)
        }
    }

    @Test
    fun checkIsStarredIntent_Failure() {
        val defaultState = RepositoryViewState.idle()
        whenever(reposRepository.checkIsStarred(any())).thenReturn(Single.error(expectedError))

        viewModel.processIntents(Observable.just(RepositoryIntent.CheckIsStarredIntent(expectedRepository.full_name)))

        verify(reposRepository).checkIsStarred(expectedRepository.full_name)
        testObserver.assertValueAt(1) {
            it == defaultState.copy(isLoading = true)
        }
        testObserver.assertValueAt(2) {
            it == defaultState.copy(error = expectedError)
        }
    }

    @Test
    fun starIntent_Success() {
        val defaultState = RepositoryViewState.idle()
        whenever(reposRepository.starRepository(any())).thenReturn(Completable.complete())

        viewModel.processIntents(Observable.just(RepositoryIntent.StarIntent(expectedRepository.full_name)))

        verify(reposRepository).starRepository(expectedRepository.full_name)
        testObserver.assertValueAt(1) {
            it == defaultState.copy(
                    isStarred = true,
                    repository = defaultState.repository?.plusStarCount()
            )
        }
    }

    @Test
    fun starIntent_Failure() {
        val defaultState = RepositoryViewState.idle()
        whenever(reposRepository.starRepository(any())).thenReturn(Completable.error(expectedError))

        viewModel.processIntents(Observable.just(RepositoryIntent.StarIntent(expectedRepository.full_name)))

        verify(reposRepository).starRepository(expectedRepository.full_name)
        testObserver.assertValueAt(1) {
            it == defaultState.copy(error = expectedError)
        }
    }

    @Test
    fun unStarIntent_Success() {
        val defaultState = RepositoryViewState.idle()
        whenever(reposRepository.unStarRepository(any())).thenReturn(Completable.complete())

        viewModel.processIntents(Observable.just(RepositoryIntent.UnStarIntent(expectedRepository.full_name)))

        verify(reposRepository).unStarRepository(expectedRepository.full_name)
        testObserver.assertValueAt(1) {
            it == defaultState.copy(
                    isStarred = false,
                    repository = defaultState.repository?.minusStarCount()
            )
        }
    }

    @Test
    fun unStarIntent_Failure() {
        val defaultState = RepositoryViewState.idle()
        whenever(reposRepository.unStarRepository(any())).thenReturn(Completable.error(expectedError))

        viewModel.processIntents(Observable.just(RepositoryIntent.UnStarIntent(expectedRepository.full_name)))

        verify(reposRepository).unStarRepository(expectedRepository.full_name)
        testObserver.assertValueAt(1) {
            it == defaultState.copy(error = expectedError)
        }
    }
}