package com.itome.githubmvi.ui.userdetail.core

import com.itome.githubmvi.mvibase.MviIntent

sealed class UserDetailIntent : MviIntent {

    data class FetchUserIntent(val userName: String) : UserDetailIntent()

    data class FetchUserReposIntent(val userName: String) : UserDetailIntent()

    data class CheckIsFollowedIntent(val userName: String) : UserDetailIntent()

    object FollowIntent : UserDetailIntent()

    object UnFollowIntent : UserDetailIntent()

}