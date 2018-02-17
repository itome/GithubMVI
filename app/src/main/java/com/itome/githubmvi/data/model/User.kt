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

) : RealmObject() {

    fun plusFollowerCount(): User {
        return User(
                id = this.id,
                login = this.login,
                name = this.name,
                avatar_url = this.avatar_url,
                bio = this.bio,
                email = this.email,
                followers = this.followers + 1,
                following = this.following
        )
    }

    fun minulFollowerCount(): User {
        return User(
                id = this.id,
                login = this.login,
                name = this.name,
                avatar_url = this.avatar_url,
                bio = this.bio,
                email = this.email,
                followers = this.followers - 1,
                following = this.following
        )
    }
}