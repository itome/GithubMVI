package com.itome.githubmvi.ui.userdetail

import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.itome.githubmvi.R
import com.itome.githubmvi.data.model.Repository
import com.itome.githubmvi.extensions.getContextColor
import com.itome.githubmvi.extensions.setVisibility
import io.reactivex.subjects.PublishSubject
import org.jetbrains.anko.*
import org.jetbrains.anko.cardview.v7.cardView

class UserReposAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var repos: List<Repository> = emptyList()

    val itemViewClickPublisher = PublishSubject.create<String>()!!

    override fun getItemCount(): Int = repos.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        lateinit var nameTextView: TextView
        lateinit var starCountTextView: TextView
        lateinit var forkCountTextView: TextView
        lateinit var descriptionTextView: TextView

        val itemView = parent.context.cardView {
            cardElevation = dip(4).toFloat()
            useCompatPadding = true
            lparams(matchParent, wrapContent)

            verticalLayout {
                linearLayout {
                    gravity = Gravity.CENTER_VERTICAL
                    lparams(matchParent, wrapContent) {
                        leftMargin = dip(16)
                        topMargin = dip(8)
                        rightMargin = dip(16)
                        bottomMargin = dip(8)
                    }

                    nameTextView = textView {
                        textSize = 18F
                        textColor = context.getContextColor(R.color.black)
                    }

                    imageView(R.drawable.ic_star_filled).lparams(dip(16), dip(16)) {
                        leftMargin = dip(16)
                    }
                    starCountTextView = textView {
                        textSize = 14F
                        textColor = context.getContextColor(R.color.gray)
                    }.lparams { leftMargin = dip(8) }

                    imageView(R.drawable.ic_fork).lparams(dip(12), dip(12)) {
                        leftMargin = dip(12)
                    }
                    forkCountTextView = textView {
                        textSize = 12F
                        textColor = context.getContextColor(R.color.gray)
                    }
                }

                descriptionTextView = textView {
                    textSize = 14F
                    textColor = context.getContextColor(R.color.gray)
                }.lparams(matchParent, wrapContent) {
                    leftMargin = dip(16)
                    rightMargin = dip(16)
                    bottomMargin = dip(8)
                }

            }
        }
        return ViewHolder(itemView, nameTextView, starCountTextView, forkCountTextView, descriptionTextView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (holder is ViewHolder) {
            holder.repository = repos[position]
        }
    }

    inner class ViewHolder(
            itemView: View,
            private val nameTextView: TextView,
            private val starCountTextView: TextView,
            private val forkCountTextView: TextView,
            private val descriptionTextView: TextView
    ) : RecyclerView.ViewHolder(itemView) {
        var repository: Repository = Repository()
            set(value) {
                field = value
                nameTextView.text = value.name
                starCountTextView.text = value.stargazers_count.toString()
                forkCountTextView.text = value.forks_count.toString()
                descriptionTextView.text = value.description
            }

        init {
            itemView.setOnClickListener { itemViewClickPublisher.onNext(repository.name) }
        }
    }
}