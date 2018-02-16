package com.itome.githubmvi.ui.repository

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import org.jetbrains.anko.setContentView

class RepositoryActivity : AppCompatActivity() {

    companion object {
        const val REPOSITORY_ID = "repository_id"
    }

    private val id by lazy { intent.getIntExtra(REPOSITORY_ID, 0) }
    private val ui by lazy { RepositoryActivityUI() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ui.setContentView(this)
    }
}
