package com.itome.githubmvi.splash

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.itome.githubmvi.BuildConfig
import com.itome.githubmvi.mvibase.MviView
import com.itome.githubmvi.oauth2.OAuth2Activity
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import org.jetbrains.anko.setContentView
import org.jetbrains.anko.startActivityForResult

class SplashActivity : AppCompatActivity(), MviView<SplashIntent, SplashViewState> {

    companion object {
        private const val APP_NAME = "githubmvi"
        private const val ACCESS_TOKEN = "access_token"
        private const val OAUTH2_URL = "https://github.com/login/oauth/authorize?client_id="
        private const val REQUEST_OAUTH = 0
    }

    private val ui by lazy { SplashActivityUI() }
    private val viewModel: SplashViewModel by lazy { SplashViewModel(SplashActionProcessorHolder()) }
    private val fetchAccessTokenIntentPublisher = PublishSubject.create<SplashIntent.FetchAccessTokenIntent>()
    private val fetchSelfDataIntentPublisher = PublishSubject.create<SplashIntent.FetchSelfDataIntent>()
    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui.setContentView(this)

        val prefs = getSharedPreferences(APP_NAME, Context.MODE_PRIVATE)
        val accessToken = prefs.getString(ACCESS_TOKEN, "")
        if (accessToken == "") {
            startActivityForResult<OAuth2Activity>(
                    REQUEST_OAUTH,
                    OAuth2Activity.URL to OAUTH2_URL + BuildConfig.CLIENT_ID)
        } else {
            fetchSelfDataIntentPublisher.onNext(SplashIntent.FetchSelfDataIntent(accessToken))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_OAUTH && resultCode == Activity.RESULT_OK) {
            val code = data?.getStringExtra(OAuth2Activity.CODE) ?: return
            fetchAccessTokenIntentPublisher.onNext(
                    SplashIntent.FetchAccessTokenIntent(
                            BuildConfig.CLIENT_ID,
                            BuildConfig.CLIENT_SECRET,
                            code)
            )
        }
    }

    override fun onStart() {
        super.onStart()

        bind()
    }

    override fun onDestroy() {
        super.onDestroy()

        disposables.dispose()
    }

    private fun bind() {
        disposables.add(viewModel.states().subscribe(this::render, {}))
        viewModel.processIntents(intents())
    }

    override fun intents(): Observable<SplashIntent> {
        return Observable.merge(
                initialIntent(),
                fetchAccessTokenIntent(),
                fetchSelfDataIntent()
        )
    }

    override fun render(state: SplashViewState) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun initialIntent(): Observable<SplashIntent.InitialIntent> {
        return Observable.just(SplashIntent.InitialIntent)
    }

    private fun fetchAccessTokenIntent(): Observable<SplashIntent.FetchAccessTokenIntent> {
        return fetchAccessTokenIntentPublisher
    }

    private fun fetchSelfDataIntent(): Observable<SplashIntent.FetchSelfDataIntent> {
        return fetchSelfDataIntentPublisher
    }
}
