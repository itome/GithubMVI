package com.itome.githubmvi.data.model

open class Event(

        var id: String = "",

        var type: String = "",

        var repo: EventRepo? = null,

        var actor: EventActor? = null,

        var created_at: String = ""

) {

    data class EventActor(

            var id: Int = 0,

            var login: String = "",

            var avatar_url: String = "",

            var url: String = ""

    )

    data class EventRepo(

            var id: Int = 0,

            var name: String = "",

            var url: String = ""

    )
}
