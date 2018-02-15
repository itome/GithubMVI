package com.itome.githubmvi.data.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class Repository(

        @PrimaryKey
        var id: String = "",

        var owner: User? = null,

        var name: String = "",

        var full_name: String = "",

        var forks_count: Int = 0,

        var stargazers_count: Int = 0,

        var watchers_count: Int = 0

) : RealmObject()