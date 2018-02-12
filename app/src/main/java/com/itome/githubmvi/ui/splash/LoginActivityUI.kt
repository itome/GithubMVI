package com.itome.githubmvi.ui.splash

import android.view.Gravity
import android.widget.Button
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.button
import org.jetbrains.anko.frameLayout

class LoginActivityUI : AnkoComponent<LoginActivity> {

    lateinit var loginButton: Button

    override fun createView(ui: AnkoContext<LoginActivity>) = with(ui) {
        frameLayout {
            loginButton = button("OAUTH").lparams {
                gravity = Gravity.CENTER
            }
        }
    }
}