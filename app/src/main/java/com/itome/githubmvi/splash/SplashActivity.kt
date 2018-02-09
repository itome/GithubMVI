package com.itome.githubmvi.splash

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.itome.githubmvi.BuildConfig
import com.itome.githubmvi.oauth2.OAuth2Activity
import org.jetbrains.anko.startActivityForResult

class SplashActivity : AppCompatActivity() {

    companion object {
        private const val APP_NAME = "githubmvi"
        private const val ACCESS_TOKEN = "access_token"
        private const val OAUTH2_URL = "https://github.com/login/oauth/authorize?client_id="
        private const val REQUEST_OAUTH = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences(APP_NAME, Context.MODE_PRIVATE)
        if (prefs.getString(ACCESS_TOKEN, "") == "") {
            startActivityForResult<OAuth2Activity>(
                    REQUEST_OAUTH,
                    OAuth2Activity.URL to OAUTH2_URL + BuildConfig.CLIENT_ID)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_OAUTH && resultCode == Activity.RESULT_OK) {
            val code = data?.getStringExtra(OAuth2Activity.CODE)
        }
    }
}
