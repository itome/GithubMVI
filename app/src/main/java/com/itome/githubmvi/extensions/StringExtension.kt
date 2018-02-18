package com.itome.githubmvi.extensions

import android.util.Base64

fun String.decodeMarkDown(): String {
    return String(Base64.decode(this, Base64.DEFAULT))
}