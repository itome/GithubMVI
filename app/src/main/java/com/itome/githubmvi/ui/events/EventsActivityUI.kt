package com.itome.githubmvi.ui.events

import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.itome.githubmvi.ui.events.core.EventsViewState
import io.reactivex.subjects.PublishSubject
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.frameLayout
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.swipeRefreshLayout


class EventsActivityUI : AnkoComponent<EventsActivity> {

    private val eventsAdapter = EventsAdapter()

    val userImageClickPublisher = eventsAdapter.userImageClickPublisher
    val itemViewClickPublisher = eventsAdapter.itemViewClickPublisher
    val refreshPublisher = PublishSubject.create<View>()!!
    val loadMorePublisher = PublishSubject.create<View>()!!

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    fun applyState(state: EventsViewState) {
        eventsAdapter.events = state.events
        eventsAdapter.notifyDataSetChanged()
        swipeRefreshLayout.isRefreshing = state.isLoading
    }

    override fun createView(ui: AnkoContext<EventsActivity>) = with(ui) {
        frameLayout {
            swipeRefreshLayout = swipeRefreshLayout {
                setOnRefreshListener { refreshPublisher.onNext(this) }
                recyclerView {
                    layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    adapter = eventsAdapter

                    addOnScrollListener(object : EndlessOnScrollListener(
                            layoutManager as LinearLayoutManager
                    ) {
                        override fun onLoadMore() {
                            loadMorePublisher.onNext(view)
                        }
                    })
                }
            }
        }
    }

    abstract inner class EndlessOnScrollListener(
            private val linearLayoutManager: LinearLayoutManager
    ) : RecyclerView.OnScrollListener() {

        private val visibleThreshold = 5
        private var previousTotal = 0
        private var loading = true

        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val visibleItemCount = recyclerView!!.childCount
            val totalItemCount = linearLayoutManager.itemCount
            val firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition()

            if (loading) {
                if (totalItemCount > previousTotal) {
                    loading = false
                    previousTotal = totalItemCount
                }
            }
            if (!loading && totalItemCount - visibleItemCount <= firstVisibleItem + visibleThreshold) {
                onLoadMore()
                loading = true
            }
        }

        abstract fun onLoadMore()
    }
}
