package com.itome.githubmvi.ui.splash

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.itome.githubmvi.BuildConfig
import com.itome.githubmvi.data.datastore.local.LoginLocalDataStore
import com.itome.githubmvi.data.datastore.remote.LoginRemoteDataStore
import com.itome.githubmvi.data.repository.LoginRepository
import com.itome.githubmvi.extensions.setVisibility
import com.itome.githubmvi.mvibase.MviView
import com.itome.githubmvi.ui.oauth2.OAuth2Activity
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import org.jetbrains.anko.setContentView
import org.jetbrains.anko.startActivityForResult

class LoginActivity : AppCompatActivity(), MviView<LoginIntent, LoginViewState> {

    companion object {
        private const val REQUEST_OAUTH = 0
    }

    private val ui by lazy { LoginActivityUI() }
    private val viewModel: LoginViewModel by lazy {
        LoginViewModel(LoginActionProcessorHolder(
                LoginRepository(LoginLocalDataStore(), LoginRemoteDataStore())
        ))
    }
    private val fetchAccessTokenIntentPublisher = PublishSubject.create<LoginIntent.FetchAccessTokenIntent>()
    private val fetchLoginDataIntentPublisher = PublishSubject.create<LoginIntent.FetchLoginDataIntent>()
    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui.setContentView(this)

        ui.loginButton.setOnClickListener {
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
        ui.loginButton.setVisibility(state.needsAccessToken)
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
