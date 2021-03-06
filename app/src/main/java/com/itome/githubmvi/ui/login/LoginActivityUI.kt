package com.itome.githubmvi.ui.login

import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.itome.githubmvi.R
import com.itome.githubmvi.extensions.getContextColor
import com.itome.githubmvi.extensions.setVisibility
import com.itome.githubmvi.ui.circleImageView
import com.itome.githubmvi.ui.login.core.LoginViewState
import io.reactivex.subjects.PublishSubject
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.button
import org.jetbrains.anko.dip
import org.jetbrains.anko.frameLayout
import org.jetbrains.anko.textColor
import org.jetbrains.anko.textView
import org.jetbrains.anko.verticalLayout
import org.jetbrains.anko.wrapContent

class LoginActivityUI : AnkoComponent<LoginActivity> {

    private lateinit var loginButton: Button
    private lateinit var githubImageView: ImageView

    private lateinit var userImageView: ImageView
    private lateinit var welcomeTextView: TextView

    val oauthButtonClickPublisher = PublishSubject.create<View>()!!

    fun applyState(state: LoginViewState) {
        loginButton.setVisibility(state.needsAccessToken)
        githubImageView.setVisibility(state.needsAccessToken)
        userImageView.setVisibility(!state.needsAccessToken)
        welcomeTextView.setVisibility(!state.needsAccessToken)

        Glide.with(userImageView.context)
            .load(state.userImageUrl)
            .apply(RequestOptions().placeholder(R.color.gray))
            .into(userImageView)
        welcomeTextView.text = if (state.isLoading) {
            welcomeTextView.context.getString(R.string.fetching_user_data)
        } else {
            welcomeTextView.context.getString(R.string.welcome_message, state.userName)
        }
    }

    override fun createView(ui: AnkoContext<LoginActivity>) = with(ui) {
        frameLayout {
            verticalLayout {
                gravity = Gravity.CENTER_HORIZONTAL

                userImageView = circleImageView().lparams {
                    width = dip(120)
                    height = dip(120)
                    bottomMargin = dip(32)
                }

                welcomeTextView = textView {
                    textColor = context.getContextColor(R.color.black)
                    textSize = 14F
                }.lparams {
                    width = wrapContent
                    height = wrapContent
                }
            }.lparams {
                width = wrapContent
                height = wrapContent
                gravity = Gravity.CENTER
            }

            verticalLayout {
                gravity = Gravity.CENTER_HORIZONTAL

                githubImageView = circleImageView {
                    setImageResource(R.drawable.img_github)
                }.lparams {
                    width = dip(120)
                    height = dip(120)
                    bottomMargin = dip(32)
                }

                loginButton = button {
                    text = context.getText(R.string.authorize_app)
                    textColor = context.getContextColor(R.color.white)
                    textSize = 16F
                    setPadding(dip(16), 0, dip(16), 0)
                    backgroundResource = R.drawable.bg_rounded_corner_16dp_green
                    setOnClickListener { oauthButtonClickPublisher.onNext(this) }
                }.lparams {
                    width = wrapContent
                }
            }.lparams {
                width = wrapContent
                height = wrapContent
                gravity = Gravity.CENTER
            }
        }
    }
}