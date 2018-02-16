@file:Suppress("NOTHING_TO_INLINE")

package com.itome.githubmvi.ui.widget

import android.support.v7.widget.CardView
import android.view.ViewManager
import de.hdodenhof.circleimageview.CircleImageView
import org.jetbrains.anko.custom.ankoView

inline fun ViewManager.circleImageView(theme: Int = 0) = circleImageView(theme) {}
inline fun ViewManager.circleImageView(
        theme: Int = 0,
        init: CircleImageView.() -> Unit
): CircleImageView = ankoView({ CircleImageView(it) }, theme, init)

inline fun ViewManager.cardView(theme: Int = 0) = cardView(theme) {}
inline fun ViewManager.cardView(
        theme: Int = 0,
        init: CardView.() -> Unit
): CardView = ankoView({ CardView(it) }, theme, init)
