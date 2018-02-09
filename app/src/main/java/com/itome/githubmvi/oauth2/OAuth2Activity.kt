package com.itome.githubmvi.oauth2

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import org.jetbrains.anko.frameLayout
import org.jetbrains.anko.webView

class OAuth2Activity : AppCompatActivity() {

    companion object {
        const val URL = "url"
        const val CODE = "code"
    }

    private lateinit var webView: WebView
    private val url by lazy { intent.getStringExtra(URL) }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        frameLayout {
            webView = webView {
                settings.javaScriptEnabled = true
                webViewClient = OAuth2WebViewClient()
                webChromeClient = WebChromeClient()
            }
        }

        webView.loadUrl(url)
    }

    inner class OAuth2WebViewClient: WebViewClient() {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            url ?: return
            if (url.startsWith("github-mvi://")) {
                val code = Uri.parse(url).getQueryParameter("code")
                val data = Intent().also { it.putExtra(CODE, code) }
                setResult(Activity.RESULT_OK, data)
                finish()
            }
        }
    }
}
