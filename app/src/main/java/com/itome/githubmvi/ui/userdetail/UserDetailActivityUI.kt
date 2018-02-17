package com.itome.githubmvi.ui.userdetail

import android.graphics.Color.WHITE
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v7.widget.LinearLayoutManager
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.itome.githubmvi.R
import com.itome.githubmvi.ui.userdetail.core.UserDetailViewState
import org.jetbrains.anko.*
import org.jetbrains.anko.design.appBarLayout
import org.jetbrains.anko.design.collapsingToolbarLayout
import org.jetbrains.anko.design.coordinatorLayout
import org.jetbrains.anko.design.themedAppBarLayout
import org.jetbrains.anko.recyclerview.v7.recyclerView


class UserDetailActivityUI : AnkoComponent<UserDetailActivity> {

    private val reposAdapter = UserReposAdapter()
    private lateinit var headerImageView: ImageView

    val repositoryClickPublisher = reposAdapter.itemViewClickPublisher

    fun applyState(state: UserDetailViewState) {
        state.repos?.let { repos ->
            reposAdapter.repos = repos
            reposAdapter.notifyDataSetChanged()
        }
        Glide.with(headerImageView.context)
                .load(state.user?.avatar_url)
                .apply(RequestOptions().placeholder(R.color.gray))
                .into(headerImageView)
    }

    override fun createView(ui: AnkoContext<UserDetailActivity>) = with(ui) {
        coordinatorLayout {
            lparams(matchParent, matchParent)
            fitsSystemWindows = true

            appBarLayout {
                lparams(matchParent, dip(300))
                fitsSystemWindows = true

                collapsingToolbarLayout {
                    fitsSystemWindows = true

                    toolbar {
                        setTitleTextAppearance(context, R.style.TextAppearance_AppCompat)
                        popupTheme = R.style.AppTheme
                    }.lparams(matchParent, dip(56)) {
                        collapseMode = CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PIN
                    }

                    headerImageView = imageView {
                        fitsSystemWindows = true
                        scaleType = ImageView.ScaleType.CENTER_CROP
                    }.lparams(matchParent, matchParent) {
                        collapseMode = CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PARALLAX
                    }
                }.lparams(matchParent, matchParent) {
                    scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED
                }
            }

            recyclerView {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter = reposAdapter
            }.lparams(matchParent, matchParent) {
                behavior = AppBarLayout.ScrollingViewBehavior()
            }
        }
    }
}
