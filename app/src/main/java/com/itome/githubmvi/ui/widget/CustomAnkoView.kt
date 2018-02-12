package com.itome.githubmvi.ui.widget

import android.view.ViewManager
import de.hdodenhof.circleimageview.CircleImageView
import org.jetbrains.anko.custom.ankoView

inline fun ViewManager.circleImageView(theme: Int = 0) = circleImageView(theme) {}

inline fun ViewManager.circleImageView(
        theme: Int = 0,
        init: CircleImageView.() -> Unit
): CircleImageView = ankoView({ CircleImageView(it) }, theme, init)
