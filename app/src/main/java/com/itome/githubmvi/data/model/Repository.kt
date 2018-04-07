package com.itome.githubmvi.data.model

data class Repository(

    var id: Int = 0,

    var owner: Owner? = null,

    var name: String = "",

    var full_name: String = "",

    var description: String? = "",

    var forks_count: Int = 0,

    var stargazers_count: Int = 0,

    var subscribers_count: Int = 0

) {

    data class Owner(

        var id: Int = 0,

        var login: String = "",

        var avatar_url: String = ""
    )

    fun plusStarCount(): Repository {
        return copy(stargazers_count = this.stargazers_count + 1)
    }

    fun minusStarCount(): Repository {
        return copy(stargazers_count = this.stargazers_count - 1)
    }
}