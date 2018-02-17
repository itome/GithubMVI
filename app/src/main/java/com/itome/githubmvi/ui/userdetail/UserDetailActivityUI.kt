package com.itome.githubmvi.ui.userdetail

import android.content.res.ColorStateList
import android.graphics.drawable.ColorDrawable
import android.support.design.widget.AppBarLayout
import android.support.design.widget.AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED
import android.support.design.widget.AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PARALLAX
import android.support.design.widget.CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PIN
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.itome.githubmvi.R
import com.itome.githubmvi.extensions.getContextColor
import com.itome.githubmvi.extensions.getResourceId
import com.itome.githubmvi.extensions.setVisibility
import com.itome.githubmvi.ui.userdetail.core.UserDetailViewState
import com.itome.githubmvi.ui.widget.circleImageView
import io.reactivex.subjects.PublishSubject
import jp.wasabeef.glide.transformations.BlurTransformation
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.toolbar
import org.jetbrains.anko.design.collapsingToolbarLayout
import org.jetbrains.anko.design.coordinatorLayout
import org.jetbrains.anko.design.floatingActionButton
import org.jetbrains.anko.design.themedAppBarLayout
import org.jetbrains.anko.recyclerview.v7.recyclerView


class UserDetailActivityUI : AnkoComponent<UserDetailActivity> {

    val repositoryClickPublisher
        get() = reposAdapter.itemViewClickPublisher
    val followClickPublisher = PublishSubject.create<String>()!!
    val unFollowClickPublisher = PublishSubject.create<String>()!!

    lateinit var toolbar: Toolbar
    private val fabAnchorId = View.generateViewId()
    private lateinit var headerImageView: ImageView
    private lateinit var emailImageView: ImageView
    private lateinit var userCircleImageView: ImageView
    private lateinit var loginNameTextView: TextView
    private lateinit var fullNameTextView: TextView
    private lateinit var dividerTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var followingCountView: TextView
    private lateinit var followerCountView: TextView
    private lateinit var fab: FloatingActionButton
    private val reposAdapter = UserReposAdapter()

    fun applyState(state: UserDetailViewState) {
        Glide.with(headerImageView.context)
                .load(state.user?.avatar_url)
                .apply(RequestOptions().placeholder(R.color.black))
                .apply(RequestOptions.bitmapTransform(BlurTransformation(100)))
                .into(headerImageView)
        Glide.with(userCircleImageView)
                .load(state.user?.avatar_url)
                .apply(RequestOptions().placeholder(R.color.gray))
                .into(userCircleImageView)

        loginNameTextView.text = state.user?.login
        fullNameTextView.text = state.user?.name
        emailTextView.text = state.user?.email
        followingCountView.text = state.user?.following?.toString() ?: "0"
        followerCountView.text = state.user?.followers?.toString() ?: "0"
        dividerTextView.setVisibility(state.user?.email?.isNotEmpty() ?: false)
        emailImageView.setVisibility(state.user?.email?.isNotEmpty() ?: false)

        if (state.isFollowed) {
            fab.backgroundTintList = ColorStateList.valueOf(fab.context.getContextColor(R.color.red))
            fab.imageResource = R.drawable.ic_unfollow
            fab.setOnClickListener { unFollowClickPublisher.onNext(state.user?.login ?: "") }
        } else {
            fab.backgroundTintList = ColorStateList.valueOf(fab.context.getContextColor(R.color.green))
            fab.imageResource = R.drawable.ic_follow
            fab.setOnClickListener { followClickPublisher.onNext(state.user?.login ?: "") }
        }

        state.repos?.let { repos ->
            reposAdapter.repos = repos
            reposAdapter.notifyDataSetChanged()
        }
    }

    override fun createView(ui: AnkoContext<UserDetailActivity>) = with(ui) {
        coordinatorLayout {
            lparams(matchParent, matchParent)

            themedAppBarLayout(R.style.ThemeOverlay_AppCompat_Dark_ActionBar) {
                id = fabAnchorId
                lparams(matchParent, dip(300))
                collapsingToolbarLayout {
                    contentScrim = ColorDrawable(context.getContextColor(R.color.black))
                    expandedTitleMarginStart = dip(20)
                    expandedTitleMarginEnd = dip(20)
                    setExpandedTitleTextAppearance(R.style.SessionTitleExpanded)

                    frameLayout {
                        layoutParams = CollapsingToolbarLayout.LayoutParams(matchParent, matchParent).apply {
                            collapseMode = COLLAPSE_MODE_PARALLAX
                        }

                        headerImageView = imageView {
                            scaleType = ImageView.ScaleType.CENTER_CROP
                        }.lparams(matchParent, matchParent)

                        verticalLayout {
                            gravity = Gravity.CENTER_HORIZONTAL

                            userCircleImageView = circleImageView {
                                transitionName = "userImage"
                            }.lparams(dip(120), dip(120)) {
                                topMargin = dip(36)
                            }
                            loginNameTextView = textView {
                                elevation = dip(2).toFloat()
                                textSize = 24F
                                textColor = context.getContextColor(R.color.white)
                            }.lparams(wrapContent, wrapContent)

                            linearLayout {
                                lparams(wrapContent, wrapContent)
                                gravity = Gravity.BOTTOM
                                fullNameTextView = textView {
                                    textSize = 14F
                                    textColor = context.getContextColor(R.color.white)
                                }.lparams(wrapContent, wrapContent)
                                dividerTextView = textView(" / ") {
                                    textSize = 14F
                                    textColor = context.getContextColor(R.color.white)
                                }.lparams(wrapContent, wrapContent)
                                emailImageView = imageView(R.drawable.ic_email).lparams(dip(16), dip(16))
                                emailTextView = textView {
                                    textSize = 14F
                                    textColor = context.getContextColor(R.color.white)
                                }.lparams(wrapContent, wrapContent) { leftMargin = dip(4) }
                            }

                            linearLayout {
                                lparams(wrapContent, wrapContent)

                                verticalLayout {
                                    lparams(wrapContent, wrapContent) { margin = dip(8) }
                                    gravity = Gravity.CENTER_HORIZONTAL

                                    followerCountView = textView {
                                        textSize = 32F
                                        textColor = context.getContextColor(R.color.white)
                                    }.lparams(wrapContent, wrapContent)
                                    textView(R.string.followers) {
                                        textSize = 14F
                                        textColor = context.getContextColor(R.color.white)
                                    }.lparams(wrapContent, wrapContent)
                                }

                                verticalLayout {
                                    lparams(wrapContent, wrapContent) { margin = dip(8) }
                                    gravity = Gravity.CENTER_HORIZONTAL

                                    followingCountView = textView {
                                        textSize = 32F
                                        textColor = context.getContextColor(R.color.white)
                                    }.lparams(wrapContent, wrapContent)
                                    textView(R.string.following) {
                                        textSize = 14F
                                        textColor = context.getContextColor(R.color.white)
                                    }.lparams(wrapContent, wrapContent)
                                }
                            }
                        }
                    }

                    toolbar = toolbar {
                        title = ""
                        layoutParams = CollapsingToolbarLayout.LayoutParams(
                                matchParent,
                                dimen(context.getResourceId(R.attr.actionBarSize))
                        ).apply {
                            collapseMode = COLLAPSE_MODE_PIN
                        }
                    }
                }.lparams(width = matchParent, height = matchParent) {
                    scrollFlags = SCROLL_FLAG_SCROLL or SCROLL_FLAG_EXIT_UNTIL_COLLAPSED
                }
            }

            fab = floatingActionButton().lparams {
                anchorId = fabAnchorId
                anchorGravity = Gravity.BOTTOM or Gravity.END
                margin = dip(8)
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
