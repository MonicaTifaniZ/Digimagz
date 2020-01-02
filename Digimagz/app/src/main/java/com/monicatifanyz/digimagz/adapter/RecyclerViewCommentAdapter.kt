package com.monicatifanyz.digimagz.adapter

import android.content.Context
import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.monicatifanyz.digimagz.R
import com.monicatifanyz.digimagz.model.CommentModel
import kotlinx.android.synthetic.main.list_comment.view.*
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class RecyclerViewCommentAdapter(
    var commentModelArrayList: ArrayList<CommentModel>,
    var context: Context
) : RecyclerView.Adapter<RecyclerViewCommentAdapter.ViewHolder>() {

    private lateinit var simpleDateFormat: SimpleDateFormat
    private lateinit var date: Date
    val INTENT_PARAM_KEY_NEWS_DATA: String = "INTENT_PARAM_KEY_NEWS_DATA"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.list_comment, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return commentModelArrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val commentModel = commentModelArrayList[position]

        simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        try {
            date = simpleDateFormat.parse(commentModel.dateComment)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        if (commentModel.profilpicUrl != null) {
            Glide.with(context)
                .asBitmap()
                .load(commentModel.profilpicUrl)
                .placeholder(R.color.chef)
                .into(holder.circleImageViewComment)
        }

        holder.textViewCommentUser.setText(commentModel.email)
        holder.textViewCommentContent.setText(commentModel.commentText)
        holder.textViewCommentDate.text = DateFormat.getDateInstance(
            DateFormat.LONG,
            Locale("in", "ID")
        ).format(date)

        if (commentModel.adminReply != null) {
            holder.linearLayoutReply.setVisibility(View.VISIBLE)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                holder.textViewCommentContentAdmin.setText(
                    Html.fromHtml(
                        commentModel.adminReply,
                        Html.FROM_HTML_MODE_COMPACT
                    )
                )
            } else {
                holder.textViewCommentContentAdmin.setText(Html.fromHtml(commentModel.adminReply))
            }
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val circleImageViewComment = view.circleImageViewComment
        val textViewCommentUser = view.textViewCommentUser
        val textViewCommentContent = view.textViewCommentContent
        val textViewCommentDate = view.textViewCommentDate
        val textViewCommentContentAdmin = view.textViewCommentContentAdmin
        val linearLayoutReply = view.linearLayoutReply
    }

}