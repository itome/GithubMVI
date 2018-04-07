package com.itome.githubmvi.data.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class LoginData(

    @PrimaryKey
    var id: Int = 0,

    var accessToken: String = "",

    var loginUser: User? = null

) : RealmObject()