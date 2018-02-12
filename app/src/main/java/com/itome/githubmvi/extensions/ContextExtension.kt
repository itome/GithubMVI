package com.itome.githubmvi.extensions

import android.content.Context
import android.support.v4.content.ContextCompat

fun Context.getContextColor(resId: Int): Int {
    return ContextCompat.getColor(this, resId)
}