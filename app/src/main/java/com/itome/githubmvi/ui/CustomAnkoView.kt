@file:Suppress("NOTHING_TO_INLINE")

package com.itome.githubmvi.ui

import android.view.ViewManager
import de.hdodenhof.circleimageview.CircleImageView
import org.jetbrains.anko.custom.ankoView
import ru.noties.markwon.view.MarkwonView

inline fun ViewManager.circleImageView(theme: Int = 0) = circleImageView(theme) {}
inline fun ViewManager.circleImageView(
        theme: Int = 0,
        init: CircleImageView.() -> Unit
): CircleImageView = ankoView({ CircleImageView(it) }, theme, init)

inline fun ViewManager.markdownView(theme: Int = 0) = markdownView(theme) {}
inline fun ViewManager.markdownView(
        theme: Int = 0,
        init: MarkwonView.() -> Unit
): MarkwonView = ankoView({ MarkwonView(it) }, theme, init)
