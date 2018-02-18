@file:Suppress("NOTHING_TO_INLINE")

package com.itome.githubmvi.ui

import android.view.ViewManager
import br.tiagohm.markdownview.MarkdownView
import de.hdodenhof.circleimageview.CircleImageView
import org.jetbrains.anko.custom.ankoView

inline fun ViewManager.circleImageView(theme: Int = 0) = circleImageView(theme) {}
inline fun ViewManager.circleImageView(
        theme: Int = 0,
        init: CircleImageView.() -> Unit
): CircleImageView = ankoView({ CircleImageView(it) }, theme, init)

inline fun ViewManager.markdownView(theme: Int = 0) = markdownView(theme) {}
inline fun ViewManager.markdownView(
        theme: Int = 0,
        init: MarkdownView.() -> Unit
): MarkdownView = ankoView({ MarkdownView(it) }, theme, init)
