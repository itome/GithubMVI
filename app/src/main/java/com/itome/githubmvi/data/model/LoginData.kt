package com.itome.githubmvi.data.model

import io.realm.RealmObject
import io.realm.annotations.RealmClass

@RealmClass
open class LoginData(
        var accessToken: String = "",
        var loginUser: User? = null
) : RealmObject()