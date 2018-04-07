package com.itome.githubmvi

import com.itome.githubmvi.data.model.Event
import com.itome.githubmvi.data.model.User
import com.itome.githubmvi.data.repository.EventsRepository
import com.itome.githubmvi.data.repository.LoginRepository
import com.itome.githubmvi.scheduler.ImmediateSchedulerProvider
import com.itome.githubmvi.scheduler.SchedulerProvider
import com.itome.githubmvi.ui.events.core.EventsIntent
import com.itome.githubmvi.ui.events.core.EventsProcessorHolder
import com.itome.githubmvi.ui.events.core.EventsViewModel
import com.itome.githubmvi.ui.events.core.EventsViewState
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.anyInt
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class EventsViewModelTest {

    companion object {
        private val expectedEvents = listOf(Event(), Event())
        private val expectedUser = User(id = 1, name = "testUser")
        private val expectedError = Throwable("Test error occurred!!")
    }

    @Mock
    private lateinit var loginRepository: LoginRepository
    @Mock
    private lateinit var eventsRepository: EventsRepository

    private lateinit var schedulerProvider: SchedulerProvider
    private lateinit var viewModel: EventsViewModel
    private lateinit var testObserver: TestObserver<EventsViewState>

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        schedulerProvider = ImmediateSchedulerProvider()

        viewModel = EventsViewModel(
            EventsProcessorHolder(
                eventsRepository, loginRepository, schedulerProvider
            )
        )

        testObserver = viewModel.states().test()
    }

    @Test
    fun fetchFirstPageIntent_Success() {
        val defaultState = EventsViewState.idle()
        whenever(eventsRepository.getEvents(1)).thenReturn(Single.just(expectedEvents))

        viewModel.processIntents(Observable.just(EventsIntent.FetchFirstPageIntent))

        verify(eventsRepository).getEvents(1)
        testObserver.assertValueAt(1) {
            it == defaultState.copy(isLoading = true)
        }
        testObserver.assertValueAt(2) {
            it == defaultState.copy(events = expectedEvents, nextPage = 2)
        }
    }

    @Test
    fun fetchFirstPageIntent_Failure() {
        val defaultState = EventsViewState.idle()
        whenever(eventsRepository.getEvents(1)).thenReturn(Single.error(expectedError))

        viewModel.processIntents(Observable.just(EventsIntent.FetchFirstPageIntent))

        verify(eventsRepository).getEvents(1)
        testObserver.assertValueAt(1) {
            it == defaultState.copy(isLoading = true)
        }
        testObserver.assertValueAt(2) {
            it == defaultState.copy(error = expectedError)
        }
    }

    @Test
    fun fetchPageIntent_Success() {
        val defaultState = EventsViewState.idle()
        whenever(eventsRepository.getEvents(anyInt())).thenReturn(Single.just(expectedEvents))

        viewModel.processIntents(Observable.just(EventsIntent.FetchPageIntent(2)))

        verify(eventsRepository).getEvents(2)
        testObserver.assertValueAt(1) {
            it == defaultState.copy(isLoading = true)
        }
        testObserver.assertValueAt(2) {
            it == defaultState.copy(events = expectedEvents, nextPage = defaultState.nextPage + 1)
        }
    }

    @Test
    fun fetchPageIntent_Failure() {
        val defaultState = EventsViewState.idle()
        whenever(eventsRepository.getEvents(anyInt())).thenReturn(Single.error(expectedError))

        viewModel.processIntents(Observable.just(EventsIntent.FetchPageIntent(2)))

        verify(eventsRepository).getEvents(2)
        testObserver.assertValueAt(1) {
            it == defaultState.copy(isLoading = true)
        }
        testObserver.assertValueAt(2) {
            it == defaultState.copy(error = expectedError)
        }
    }

    @Test
    fun fetchLoginUserIntent_Success() {
        val defaultState = EventsViewState.idle()
        whenever(loginRepository.getLoginUser()).thenReturn(Single.just(expectedUser))

        viewModel.processIntents(Observable.just(EventsIntent.FetchLoginUserIntent))

        verify(loginRepository).getLoginUser()
        testObserver.assertValueAt(1) {
            it == defaultState.copy(isLoading = true)
        }
        testObserver.assertValueAt(2) {
            it == defaultState.copy(loginUser = expectedUser)
        }
    }

    @Test
    fun fetchLoginUserIntent_Failure() {
        val defaultState = EventsViewState.idle()
        whenever(loginRepository.getLoginUser()).thenReturn(Single.error(expectedError))

        viewModel.processIntents(Observable.just(EventsIntent.FetchLoginUserIntent))

        verify(loginRepository).getLoginUser()
        testObserver.assertValueAt(1) {
            it == defaultState.copy(isLoading = true)
        }
        testObserver.assertValueAt(2) {
            it == defaultState.copy(error = expectedError)
        }
    }
}