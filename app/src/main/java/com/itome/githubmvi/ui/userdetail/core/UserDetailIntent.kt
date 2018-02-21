package com.itome.githubmvi.ui.userdetail.core

import com.itome.githubmvi.mvibase.MviIntent

sealed class UserDetailIntent : MviIntent {

    data class FetchUserIntent(val userName: String) : UserDetailIntent()

    data class FetchUserReposIntent(val userName: String) : UserDetailIntent()

    data class CheckIsLoginUserIntent(val userName: String) : UserDetailIntent()

    data class CheckIsFollowedIntent(val userName: String) : UserDetailIntent()

    data class FollowIntent(val userName: String) : UserDetailIntent()

    data class UnFollowIntent(val userName: String) : UserDetailIntent()

}