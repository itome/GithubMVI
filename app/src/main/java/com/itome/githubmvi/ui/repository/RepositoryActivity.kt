package com.itome.githubmvi.ui.repository

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.itome.githubmvi.di.component.DaggerRepositoryActivityComponent
import com.itome.githubmvi.di.module.ApiModule
import com.itome.githubmvi.di.module.OkHttpModule
import com.itome.githubmvi.di.module.RepositoryActivityModule
import com.itome.githubmvi.mvibase.MviView
import com.itome.githubmvi.mvibase.MviViewModel
import com.itome.githubmvi.ui.repository.core.RepositoryIntent
import com.itome.githubmvi.ui.repository.core.RepositoryIntent.*
import com.itome.githubmvi.ui.repository.core.RepositoryViewState
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import org.jetbrains.anko.setContentView
import javax.inject.Inject

class RepositoryActivity : AppCompatActivity(), MviView<RepositoryIntent, RepositoryViewState> {

    companion object {
        const val REPOSITORY_NAME = "repository_name"
    }

    @Inject
    lateinit var viewModel: MviViewModel<RepositoryIntent, RepositoryViewState>
    private val repositoryName by lazy { intent.getStringExtra(REPOSITORY_NAME) }
    private val ui by lazy { RepositoryActivityUI() }

    private val fetchRepositoryIntentPublisher = PublishSubject.create<FetchRepositoryIntent>()
    private val fetchReadmeIntentPublisher = PublishSubject.create<FetchReadmeIntent>()
    private val checkIsStarredIntentPublisher = PublishSubject.create<CheckIsStarredIntent>()
    private val starRepositoryIntentPublisher = PublishSubject.create<StarIntent>()
    private val unStarRepositoryIntent = PublishSubject.create<UnStarIntent>()
    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val component = DaggerRepositoryActivityComponent.builder()
            .repositoryActivityModule(RepositoryActivityModule())
            .apiModule(ApiModule())
            .okHttpModule(OkHttpModule())
            .build()
        component.inject(this)

        ui.setContentView(this)
        bindViewModel()

        setSupportActionBar(ui.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        disposable.add(ui.starClickPublisher.subscribe {
            starRepositoryIntentPublisher.onNext(StarIntent(repositoryName))
        })
        disposable.add(ui.unStarClickPublisher.subscribe {
            unStarRepositoryIntent.onNext(UnStarIntent(repositoryName))
        })
    }

    override fun onStart() {
        super.onStart()
        fetchRepositoryIntentPublisher.onNext(FetchRepositoryIntent(repositoryName))
        fetchReadmeIntentPublisher.onNext(FetchReadmeIntent(repositoryName))
        checkIsStarredIntentPublisher.onNext(CheckIsStarredIntent(repositoryName))
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }

    override fun intents(): Observable<RepositoryIntent> {
        return Observable.merge(
            listOf(
                fetchRepositoryIntentPublisher,
                fetchReadmeIntentPublisher,
                checkIsStarredIntentPublisher,
                starRepositoryIntentPublisher,
                unStarRepositoryIntent
            )
        )
    }

    override fun render(state: RepositoryViewState) {
        ui.applyState(state)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                finishAfterTransition()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun bindViewModel() {
        disposable.add(viewModel.states().subscribe(this::render))
        viewModel.processIntents(intents())
    }
}
