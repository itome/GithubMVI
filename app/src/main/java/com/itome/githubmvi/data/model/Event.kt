package com.itome.githubmvi.data.model

import com.itome.githubmvi.R

open class Event(

    var id: String = "",

    var type: String = "",

    var repo: EventRepo? = null,

    var actor: EventActor? = null,

    var created_at: String = ""

) {

    fun getEventType(): EventType = when (type) {
        "CommitCommentEvent" -> EventType.CommitCommentEvent
        "CreateEvent" -> EventType.CreateEvent
        "DeleteEvent" -> EventType.DeleteEvent
        "DownloadEvent" -> EventType.DownloadEvent
        "FollowEvent" -> EventType.FollowEvent
        "ForkEvent" -> EventType.ForkEvent
        "ForkApplyEvent" -> EventType.ForkApplyEvent
        "GistEvent" -> EventType.GistEvent
        "GollumEvent" -> EventType.GollumEvent
        "IssueCommentEvent" -> EventType.IssueCommentEvent
        "IssuesEvent" -> EventType.IssuesEvent
        "MemberEvent" -> EventType.MemberEvent
        "OrganizationEvent" -> EventType.OrganizationEvent
        "ProjectCardEvent" -> EventType.ProjectCardEvent
        "ProjectColumnEvent" -> EventType.ProjectColumnEvent
        "ProjectEvent" -> EventType.ProjectEvent
        "PublicEvent" -> EventType.PublicEvent
        "PullRequestEvent" -> EventType.PullRequestEvent
        "PullRequestReviewEvent" -> EventType.PullRequestReviewEvent
        "PullRequestReviewCommentEvent" -> EventType.PullRequestReviewCommentEvent
        "PushEvent" -> EventType.PushEvent
        "ReleaseEvent" -> EventType.ReleaseEvent
        "RepositoryEvent" -> EventType.RepositoryEvent
        "TeamEvent" -> EventType.TeamAddEvent
        "TeamAddEvent" -> EventType.TeamAddEvent
        "WatchEvent" -> EventType.WatchEvent
        else -> EventType.NotFoundEvent
    }

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

    enum class EventType(
        val actionResId: Int,
        val iconResId: Int
    ) {
        NotFoundEvent(R.string.did_something, R.drawable.ic_info_outline),
        WatchEvent(R.string.starred, R.drawable.ic_star_filled),
        CreateEvent(R.string.created_repo, R.drawable.ic_repo),
        CommitCommentEvent(R.string.commented_on_commit, R.drawable.ic_comment),
        DownloadEvent(R.string.downloaded, R.drawable.ic_download),
        FollowEvent(R.string.followed, R.drawable.ic_add),
        ForkEvent(R.string.forked, R.drawable.ic_fork),
        GistEvent(R.string.created_gist, R.drawable.ic_gists),
        GollumEvent(R.string.gollum, R.drawable.ic_info_outline),
        IssueCommentEvent(R.string.commented_on_issue, R.drawable.ic_comment),
        IssuesEvent(R.string.created_issue, R.drawable.ic_issues),
        MemberEvent(R.string.member, R.drawable.ic_add),
        PublicEvent(R.string.public_event, R.drawable.ic_repo),
        PullRequestEvent(R.string.pull_request, R.drawable.ic_pull_requests),
        PullRequestReviewCommentEvent(R.string.pr_comment_review, R.drawable.ic_comment),
        PullRequestReviewEvent(R.string.pr_review_event, R.drawable.ic_eye),
        RepositoryEvent(R.string.repo_event, R.drawable.ic_repo),
        PushEvent(R.string.pushed, R.drawable.ic_push),
        TeamAddEvent(R.string.team_event, R.drawable.ic_profile),
        DeleteEvent(R.string.deleted, R.drawable.ic_trash),
        ReleaseEvent(R.string.released, R.drawable.ic_download),
        ForkApplyEvent(R.string.forked, R.drawable.ic_fork),
        OrgBlockEvent(R.string.organization_event, R.drawable.ic_profile),
        ProjectCardEvent(R.string.card_event, R.drawable.ic_info_outline),
        ProjectColumnEvent(R.string.project_event, R.drawable.ic_info_outline),
        OrganizationEvent(R.string.organization_event, R.drawable.ic_profile),
        ProjectEvent(R.string.project_event, R.drawable.ic_info_outline);
    }
}
