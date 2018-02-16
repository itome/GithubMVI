package com.itome.githubmvi.data.model

open class Repository(

        var id: Int = 0,

        var name: String = "",

        var full_name: String = "",

        var forks_count: Int = 0,

        var stargazers_count: Int = 0,

        var watchers_count: Int = 0

) {

    data class Owner(

            var id: Int = 0,

            var login: String = "",

            var avatar_url: String = ""
    )

}