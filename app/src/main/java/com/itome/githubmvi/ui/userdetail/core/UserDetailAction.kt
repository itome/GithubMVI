package com.itome.githubmvi.ui.userdetail.core

import com.itome.githubmvi.mvibase.MviAction

sealed class UserDetailAction : MviAction {

    data class FetchUserAction(val userName: String) : UserDetailAction()

    data class FetchUserReposAction(val userName: String) : UserDetailAction()

    data class CheckIsFollowedAction(val userName: String) : UserDetailAction()

    data class FollowAction(val userName: String) : UserDetailAction()

    data class UnFollowAction(val userName: String) : UserDetailAction()

}