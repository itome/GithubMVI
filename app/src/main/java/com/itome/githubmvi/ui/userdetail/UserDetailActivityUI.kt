package com.itome.githubmvi.ui.userdetail

import android.graphics.Color.WHITE
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CollapsingToolbarLayout
import android.widget.ImageView
import com.itome.githubmvi.R
import com.itome.githubmvi.ui.userdetail.core.UserDetailViewState
import org.jetbrains.anko.*
import org.jetbrains.anko.design.collapsingToolbarLayout
import org.jetbrains.anko.design.coordinatorLayout
import org.jetbrains.anko.design.themedAppBarLayout
import org.jetbrains.anko.support.v4.nestedScrollView


class UserDetailActivityUI : AnkoComponent<UserDetailActivity> {

    fun applyState(state: UserDetailViewState) {
    }

    override fun createView(ui: AnkoContext<UserDetailActivity>) = with(ui) {
        coordinatorLayout {
            fitsSystemWindows = true

            themedAppBarLayout(R.style.ThemeOverlay_AppCompat_Dark_ActionBar) {
                lparams(matchParent, dip(300))
                fitsSystemWindows = true

                collapsingToolbarLayout {
                    fitsSystemWindows = true
                    expandedTitleMarginStart = dip(48)
                    expandedTitleMarginEnd = dip(64)

                    imageView(R.drawable.ic_launcher_background) {
                        fitsSystemWindows = true
                        scaleType = ImageView.ScaleType.CENTER_CROP
                    }.lparams {
                        width = matchParent
                        height = matchParent
                        collapseMode = CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PARALLAX
                    }

                    toolbar {
                        setTitleTextColor(WHITE)
                        popupTheme = R.style.AppTheme
                    }.lparams {
                        width = matchParent
                        height = R.attr.actionBarSize
                        collapseMode = CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PIN
                    }
                }.lparams(width = matchParent) {
                    width = matchParent
                    height = matchParent
                    scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED
                }
            }

            nestedScrollView {
                verticalLayout {
                    textView("Hello") { textSize = 36F }
                    textView("Hello") { textSize = 36F }
                    textView("Hello") { textSize = 36F }
                    textView("Hello") { textSize = 36F }
                    textView("Hello") { textSize = 36F }
                    textView("Hello") { textSize = 36F }
                    textView("Hello") { textSize = 36F }
                    textView("Hello") { textSize = 36F }
                    textView("Hello") { textSize = 36F }
                    textView("Hello") { textSize = 36F }
                    textView("Hello") { textSize = 36F }
                    textView("Hello") { textSize = 36F }
                    textView("Hello") { textSize = 36F }
                    textView("Hello") { textSize = 36F }
                    textView("Hello") { textSize = 36F }
                    textView("Hello") { textSize = 36F }
                    textView("Hello") { textSize = 36F }
                    textView("Hello") { textSize = 36F }
                    textView("Hello") { textSize = 36F }
                }
            }.lparams {
                width = matchParent
                height = matchParent
                behavior = AppBarLayout.ScrollingViewBehavior()
            }
        }
    }
}
