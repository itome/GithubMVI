package com.itome.githubmvi.ui.userdetail

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.itome.githubmvi.di.component.DaggerUserDetailActivityComponent
import com.itome.githubmvi.di.module.ApiModule
import com.itome.githubmvi.di.module.UserDetailActivityModule
import com.itome.githubmvi.mvibase.MviView
import com.itome.githubmvi.mvibase.MviViewModel
import com.itome.githubmvi.ui.repository.RepositoryActivity
import com.itome.githubmvi.ui.userdetail.core.UserDetailIntent
import com.itome.githubmvi.ui.userdetail.core.UserDetailIntent.FetchUserIntent
import com.itome.githubmvi.ui.userdetail.core.UserDetailIntent.FetchUserReposIntent
import com.itome.githubmvi.ui.userdetail.core.UserDetailViewState
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import org.jetbrains.anko.setContentView
import org.jetbrains.anko.startActivity
import javax.inject.Inject

class UserDetailActivity : AppCompatActivity(), MviView<UserDetailIntent, UserDetailViewState> {

    companion object {
        const val USER_NAME = "user_name"
    }

    @Inject
    lateinit var viewModel: MviViewModel<UserDetailIntent, UserDetailViewState>
    private val userName by lazy { intent.getStringExtra(USER_NAME) }
    private val ui by lazy { UserDetailActivityUI() }

    private val fetchUserIntentPublisher = PublishSubject.create<FetchUserIntent>()
    private val fetchUserReposIntentPublisher = PublishSubject.create<FetchUserReposIntent>()
    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui.setContentView(this)

        val component = DaggerUserDetailActivityComponent.builder()
                .userDetailActivityModule(UserDetailActivityModule())
                .apiModule(ApiModule())
                .build()
        component.inject(this)

        setSupportActionBar(ui.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        disposable.add(ui.repositoryClickPublisher.subscribe(this::showRepositoryActivity))
    }

    override fun onStart() {
        super.onStart()
        bind()
        fetchUserIntentPublisher.onNext(UserDetailIntent.FetchUserIntent(userName))
        fetchUserReposIntentPublisher.onNext(UserDetailIntent.FetchUserReposIntent(userName))
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
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

    override fun intents(): Observable<UserDetailIntent> {
        return Observable.merge(fetchUserIntentPublisher, fetchUserReposIntentPublisher)
    }

    override fun render(state: UserDetailViewState) {
        ui.applyState(state)
    }

    private fun bind() {
        disposable.add(viewModel.states().subscribe(this::render))
        viewModel.processIntents(intents())
    }

    private fun showRepositoryActivity(repositoryName: String) {
        startActivity<RepositoryActivity>(RepositoryActivity.REPOSITORY_NAME to repositoryName)
    }
}
