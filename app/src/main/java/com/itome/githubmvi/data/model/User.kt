package com.itome.githubmvi.data.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class User(

        @PrimaryKey
        var id: Int = 0,

        var login: String = "",

        var name: String = "",

        var avatar_url: String = "",

        var bio: String = "",

        var email: String = "",

        var followers: Int = 0,

        var following: Int = 0

) : RealmObject()