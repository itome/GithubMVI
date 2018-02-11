package com.itome.githubmvi.data.model

import io.realm.RealmObject
import io.realm.annotations.RealmClass

@RealmClass
open class User(
        var name: String = "",
        var imageUrl: String = ""
) : RealmObject()