package com.itome.githubmvi.ui.events

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.itome.githubmvi.R
import com.itome.githubmvi.data.model.Event
import com.itome.githubmvi.extensions.getContextColor
import com.itome.githubmvi.extensions.getContextDrawable
import com.itome.githubmvi.ui.circleImageView
import io.reactivex.subjects.PublishSubject
import org.jetbrains.anko.*
import org.jetbrains.anko.cardview.v7.cardView
import java.text.SimpleDateFormat
import java.util.*

class EventsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var events: List<Event> = emptyList()

    val userImageClickPublisher = PublishSubject.create<Pair<String, View>>()!!
    val itemViewClickPublisher = PublishSubject.create<String>()!!

    override fun getItemCount(): Int = events.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        lateinit var avatarImageView: ImageView
        lateinit var contentsTextView: TextView
        lateinit var iconImageView: ImageView
        lateinit var dateTextView: TextView

        val itemView = parent.context.cardView {
            cardElevation = dip(2).toFloat()
            useCompatPadding = true
            lparams(matchParent, wrapContent)

            linearLayout {
                lparams(matchParent, wrapContent)
                background = parent.context.getContextDrawable(R.drawable.bg_ripple)

                avatarImageView = circleImageView {
                    transitionName = "userImage"
                }.lparams {
                    width = dip(44)
                    height = dip(44)
                    margin = dip(12)
                    gravity = Gravity.TOP
                }

                verticalLayout {
                    lparams { margin = dip(12) }

                    contentsTextView = textView {
                        textColor = context.getContextColor(R.color.black)
                        textSize = 14F
                    }

                    linearLayout {
                        gravity = Gravity.CENTER_VERTICAL
                        lparams {
                            width = matchParent
                            topMargin = dip(8)
                        }

                        iconImageView = imageView().lparams {
                            width = dip(20)
                            height = dip(20)
                        }
                        dateTextView = textView {
                            textColor = context.getContextColor(R.color.gray)
                            textSize = 12F
                        }.lparams { leftMargin = dip(8) }
                    }
                }
            }
        }

        return ViewHolder(itemView, avatarImageView, iconImageView, contentsTextView, dateTextView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (holder is ViewHolder) {
            holder.event = events[position]
        }
    }

    inner class ViewHolder(
            itemView: View,
            private val avatarImageView: ImageView,
            private val iconImageView: ImageView,
            private val contentsTextView: TextView,
            private val dateTextView: TextView
    ) : RecyclerView.ViewHolder(itemView) {

        var event = Event()
            set(value) {
                field = value

                Glide.with(itemView.context).load(event.actor?.avatar_url)
                        .apply(RequestOptions().placeholder(R.color.gray))
                        .into(avatarImageView)

                Glide.with(itemView.context)
                        .load(event.getEventType().iconResId)
                        .into(iconImageView)

                contentsTextView.text = itemView.context.getString(R.string.event_content_text,
                        event.actor!!.login,
                        itemView.context.getString(event.getEventType().actionResId),
                        event.repo!!.name
                )

                dateTextView.text = formatDate(itemView.context, event.created_at)
            }

        init {
            avatarImageView.setOnClickListener {
                userImageClickPublisher.onNext(event.actor!!.login to avatarImageView)
            }
            itemView.setOnClickListener {
                itemViewClickPublisher.onNext(event.repo!!.name)
            }
        }

        private fun formatDate(context: Context, dateText: String): String {
            val originalFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.US)
            val date = originalFormat.parse(dateText)
            val dayDiff = ((Date().time - date.time) / (1000 * 60 * 60 * 24)).toInt()
            return when (dayDiff) {
                in 1..5 -> context.getString(R.string.day_diff, dayDiff)
                else -> dateFormat.format(date)
            }
        }
    }
}
