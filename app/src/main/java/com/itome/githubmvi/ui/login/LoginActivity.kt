package com.itome.githubmvi.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.itome.githubmvi.BuildConfig
import com.itome.githubmvi.di.component.DaggerLoginActivityComponent
import com.itome.githubmvi.di.module.LoginActivityModule
import com.itome.githubmvi.mvibase.MviView
import com.itome.githubmvi.mvibase.MviViewModel
import com.itome.githubmvi.ui.events.EventsActivity
import com.itome.githubmvi.ui.oauth2.OAuth2Activity
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import org.jetbrains.anko.setContentView
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.startActivityForResult
import javax.inject.Inject

class LoginActivity : AppCompatActivity(), MviView<LoginIntent, LoginViewState> {

    companion object {
        private const val REQUEST_OAUTH = 0
    }

    @Inject
    lateinit var viewModel: MviViewModel<LoginIntent, LoginViewState>

    private val ui by lazy { LoginActivityUI() }

    private val fetchAccessTokenIntentPublisher = PublishSubject.create<LoginIntent.FetchAccessTokenIntent>()
    private val fetchLoginDataIntentPublisher = PublishSubject.create<LoginIntent.FetchLoginDataIntent>()
    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val component = DaggerLoginActivityComponent.builder().loginActivityModule(LoginActivityModule()).build()
        component.inject(this)

        ui.setContentView(this)

        ui.oauthButtonClickPublisher.subscribe {
            val url = "https://github.com/login/oauth/authorize?client_id=${BuildConfig.CLIENT_ID}"
            startActivityForResult<OAuth2Activity>(REQUEST_OAUTH, OAuth2Activity.URL to url)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_OAUTH && resultCode == Activity.RESULT_OK) {
            val code = data?.getStringExtra(OAuth2Activity.CODE)!!
            fetchAccessTokenIntentPublisher.onNext(
                    LoginIntent.FetchAccessTokenIntent(
                            BuildConfig.CLIENT_ID,
                            BuildConfig.CLIENT_SECRET,
                            code)
            )
        }
    }

    override fun onStart() {
        super.onStart()
        bind()
        fetchLoginDataIntentPublisher.onNext(LoginIntent.FetchLoginDataIntent)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }

    override fun intents(): Observable<LoginIntent> {
        return Observable.merge(
                fetchAccessTokenIntent(),
                fetchSelfDataIntent()
        )
    }

    override fun render(state: LoginViewState) {
        if (state.startNextActivity) {
            startActivity<EventsActivity>()
            finish()
        }
        ui.applyState(state)
    }

    private fun bind() {
        disposables.add(viewModel.states().subscribe(this::render))
        viewModel.processIntents(intents())
    }

    private fun fetchAccessTokenIntent(): Observable<LoginIntent.FetchAccessTokenIntent> {
        return fetchAccessTokenIntentPublisher
    }

    private fun fetchSelfDataIntent(): Observable<LoginIntent.FetchLoginDataIntent> {
        return fetchLoginDataIntentPublisher
    }
}
