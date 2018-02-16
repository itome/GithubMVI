package com.itome.githubmvi.extensions

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat

fun Context.getContextColor(resId: Int): Int {
    return ContextCompat.getColor(this, resId)
}

fun Context.getContextDrawable(resId: Int): Drawable? {
    return ContextCompat.getDrawable(this, resId)
}