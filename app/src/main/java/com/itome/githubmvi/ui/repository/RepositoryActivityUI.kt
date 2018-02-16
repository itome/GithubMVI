package com.itome.githubmvi.ui.repository

import android.view.Gravity
import android.widget.TextView
import com.itome.githubmvi.R
import com.itome.githubmvi.extensions.getContextColor
import org.jetbrains.anko.*
import org.jetbrains.anko.cardview.v7.cardView

class RepositoryActivityUI : AnkoComponent<RepositoryActivity> {

    private lateinit var watchCountTextView: TextView
    private lateinit var starCountTextView: TextView
    private lateinit var forkCountTextView: TextView

    override fun createView(ui: AnkoContext<RepositoryActivity>) = with(ui) {
        frameLayout {
            lparams(matchParent, matchParent)

            cardView {
                linearLayout {
                    lparams {
                        width = matchParent
                        margin = dip(16)
                        gravity = Gravity.CENTER_HORIZONTAL
                    }

                    verticalLayout {
                        gravity = Gravity.CENTER_HORIZONTAL

                        watchCountTextView = textView {
                            text = "100"
                            textSize = 36F
                        }.lparams(wrapContent, wrapContent)
                        textView(R.string.watches) {
                            gravity = Gravity.CENTER
                            setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_eye, 0, 0, 0)
                        }.lparams(wrapContent, wrapContent)
                    }.lparams {
                        weight = 1F
                    }

                    view {
                        backgroundColor = context.getContextColor(R.color.gray)
                    }.lparams(dip(1), matchParent)

                    verticalLayout {
                        gravity = Gravity.CENTER_HORIZONTAL

                        starCountTextView = textView {
                            text = "100"
                            textSize = 36F
                        }.lparams(wrapContent, wrapContent)
                        textView(R.string.stars) {
                            gravity = Gravity.CENTER
                            setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_star_filled, 0, 0, 0)
                        }.lparams(wrapContent, wrapContent)
                    }.lparams {
                        weight = 1F
                    }

                    view {
                        backgroundColor = context.getContextColor(R.color.gray)
                    }.lparams(dip(1), matchParent)

                    verticalLayout {
                        gravity = Gravity.CENTER_HORIZONTAL

                        forkCountTextView = textView {
                            text = "100"
                            textSize = 36F
                        }.lparams(wrapContent, wrapContent)
                        textView(R.string.forks) {
                            gravity = Gravity.CENTER
                            setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_fork, 0, 0, 0)
                        }.lparams(wrapContent, wrapContent)
                    }.lparams {
                        weight = 1F
                    }
                }
            }
        }
    }
}