package com.itome.githubmvi.ui.repository

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import org.jetbrains.anko.setContentView

class RepositoryActivity : AppCompatActivity() {

    companion object {
        const val REPOSITORY_NAME = "repository_name"
    }

    private val repositoryName by lazy { intent.getStringExtra(REPOSITORY_NAME) }
    private val ui by lazy { RepositoryActivityUI() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ui.setContentView(this)

        setSupportActionBar(ui.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        ui.applyState()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                finishAfterTransition()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
