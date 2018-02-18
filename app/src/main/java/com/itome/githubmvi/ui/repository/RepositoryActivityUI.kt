package com.itome.githubmvi.ui.repository

import android.content.res.ColorStateList
import android.graphics.drawable.ColorDrawable
import android.support.design.widget.AppBarLayout
import android.support.design.widget.AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED
import android.support.design.widget.AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PARALLAX
import android.support.design.widget.CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PIN
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.itome.githubmvi.R
import com.itome.githubmvi.extensions.decodeMarkDown
import com.itome.githubmvi.extensions.getContextColor
import com.itome.githubmvi.extensions.getResourceId
import com.itome.githubmvi.ui.repository.core.RepositoryViewState
import com.itome.githubmvi.ui.widget.circleImageView
import com.itome.githubmvi.ui.widget.markdownView
import io.reactivex.subjects.PublishSubject
import jp.wasabeef.glide.transformations.BlurTransformation
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.toolbar
import org.jetbrains.anko.design.collapsingToolbarLayout
import org.jetbrains.anko.design.coordinatorLayout
import org.jetbrains.anko.design.floatingActionButton
import org.jetbrains.anko.design.themedAppBarLayout
import org.jetbrains.anko.support.v4.nestedScrollView
import ru.noties.markwon.Markwon
import ru.noties.markwon.view.MarkwonView

class RepositoryActivityUI : AnkoComponent<RepositoryActivity> {

    val starClickPublisher = PublishSubject.create<View>()!!
    val unStarClickPublisher = PublishSubject.create<View>()!!

    lateinit var toolbar: Toolbar
    private val fabAnchorId = View.generateViewId()
    private lateinit var watchCountTextView: TextView
    private lateinit var starCountTextView: TextView
    private lateinit var forkCountTextView: TextView
    private lateinit var fullNameTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var headerImageView: ImageView
    private lateinit var userCircleImageView: ImageView
    private lateinit var markdownView: MarkwonView
    private lateinit var fab: FloatingActionButton

    fun applyState(state: RepositoryViewState) {
        Glide.with(headerImageView)
                .load(state.repository?.owner?.avatar_url)
                .apply(RequestOptions().placeholder(R.color.black))
                .apply(RequestOptions.bitmapTransform(BlurTransformation(100)))
                .into(headerImageView)
        Glide.with(userCircleImageView)
                .load(state.repository?.owner?.avatar_url)
                .apply(RequestOptions().placeholder(R.color.gray))
                .into(userCircleImageView)

        watchCountTextView.text = state.repository?.subscribers_count?.toString()
        starCountTextView.text = state.repository?.stargazers_count?.toString()
        forkCountTextView.text = state.repository?.forks_count?.toString()
        fullNameTextView.text = state.repository?.full_name
        descriptionTextView.text = state.repository?.description

        if (state.isStarred) {
            fab.backgroundTintList = ColorStateList.valueOf(fab.context.getContextColor(R.color.red))
            fab.imageResource = R.drawable.ic_star_filled_white
            fab.setOnClickListener { unStarClickPublisher.onNext(it) }
        } else {
            fab.backgroundTintList = ColorStateList.valueOf(fab.context.getContextColor(R.color.green))
            fab.imageResource = R.drawable.ic_star_outline_white
            fab.setOnClickListener { starClickPublisher.onNext(it) }
        }

        state.readme?.let { readme ->
            Markwon.setMarkdown(markdownView, readme.content.decodeMarkDown())
        }
    }

    override fun createView(ui: AnkoContext<RepositoryActivity>) = with(ui) {
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
                            fullNameTextView = textView {
                                gravity = Gravity.CENTER_HORIZONTAL
                                textSize = 18F
                                textColor = context.getContextColor(R.color.white)
                                maxLines = 1
                                ellipsize = TextUtils.TruncateAt.END
                            }.lparams(wrapContent, wrapContent)

                            descriptionTextView = textView {
                                gravity = Gravity.CENTER_HORIZONTAL
                                textSize = 12F
                                maxLines = 2
                                textColor = context.getContextColor(R.color.white)
                            }.lparams(wrapContent, wrapContent) {
                                leftMargin = dip(32)
                                rightMargin = dip(32)
                            }

                            linearLayout {
                                lparams(wrapContent, wrapContent)

                                verticalLayout {
                                    lparams(wrapContent, wrapContent) { margin = dip(8) }
                                    gravity = Gravity.CENTER_HORIZONTAL

                                    watchCountTextView = textView {
                                        textSize = 32F
                                        textColor = context.getContextColor(R.color.white)
                                    }.lparams(wrapContent, wrapContent)
                                    textView(R.string.watches) {
                                        textSize = 14F
                                        textColor = context.getContextColor(R.color.white)
                                    }.lparams(wrapContent, wrapContent)
                                }

                                verticalLayout {
                                    lparams(wrapContent, wrapContent) { margin = dip(8) }
                                    gravity = Gravity.CENTER_HORIZONTAL

                                    starCountTextView = textView {
                                        textSize = 32F
                                        textColor = context.getContextColor(R.color.white)
                                    }.lparams(wrapContent, wrapContent)
                                    textView(R.string.stars) {
                                        textSize = 14F
                                        textColor = context.getContextColor(R.color.white)
                                    }.lparams(wrapContent, wrapContent)
                                }

                                verticalLayout {
                                    lparams(wrapContent, wrapContent) { margin = dip(8) }
                                    gravity = Gravity.CENTER_HORIZONTAL

                                    forkCountTextView = textView {
                                        textSize = 32F
                                        textColor = context.getContextColor(R.color.white)
                                    }.lparams(wrapContent, wrapContent)
                                    textView(R.string.forks) {
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

            nestedScrollView {
                verticalLayout {
                    view().lparams(matchParent, dip(32))
                    markdownView = markdownView().lparams(matchParent, matchParent) {
                        leftMargin = dip(16)
                        rightMargin = dip(16)
                    }
                }
            }.lparams(matchParent, matchParent) {
                behavior = AppBarLayout.ScrollingViewBehavior()
            }
        }
    }
}