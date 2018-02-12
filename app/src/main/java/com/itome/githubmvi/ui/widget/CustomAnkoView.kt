package com.itome.githubmvi.ui.widget

import android.view.ViewManager
import de.hdodenhof.circleimageview.CircleImageView
import org.jetbrains.anko.custom.ankoView

inline fun ViewManager.mapView(theme: Int = 0) = mapView(theme) {}

inline fun ViewManager.mapView(
        theme: Int = 0,
        init: CircleImageView.() -> Unit
): CircleImageView = ankoView({ CircleImageView(it) }, theme, init)
