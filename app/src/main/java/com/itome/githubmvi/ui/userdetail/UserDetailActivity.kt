package com.itome.githubmvi.ui.userdetail

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import org.jetbrains.anko.setContentView

class UserDetailActivity : AppCompatActivity() {

    companion object {
        const val USER_NAME = "user_name"
    }

    private val userName by lazy { intent.getStringExtra(USER_NAME) }
    private val ui by lazy { UserDetailActivityUI() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui.setContentView(this)
    }
}
