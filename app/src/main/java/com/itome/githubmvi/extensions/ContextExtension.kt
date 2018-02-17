package com.itome.githubmvi.extensions

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.annotation.AttrRes
import android.support.v4.content.ContextCompat
import android.util.TypedValue

fun Context.getContextColor(resId: Int): Int {
    return ContextCompat.getColor(this, resId)
}

fun Context.getContextDrawable(resId: Int): Drawable? {
    return ContextCompat.getDrawable(this, resId)
}

fun Context.getResourceId(@AttrRes attribute: Int) : Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(attribute, typedValue, true)
    return typedValue.resourceId
}
