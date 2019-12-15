package com.monicatifanyz.digimagz.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.monicatifanyz.digimagz.R
import com.monicatifanyz.digimagz.model.CommentModel
import kotlinx.android.synthetic.main.list_comment.view.*
import kotlinx.android.synthetic.main.list_news.view.*
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

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
        val commentModel : CommentModel = commentModelArrayList.get(position)

        simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        try {
            date = simpleDateFormat.parse(commentModel.dateComment)

        }catch (e : ParseException){
            e.printStackTrace()
        }

        var format : String? = null
        if(commentModel.profilpicUrl.equals("null")){
            if(commentModel.profilpicUrl.length>4){
                format = commentModel.profilpicUrl.substring(commentModel.profilpicUrl.length- 4)

            }

        }

        if (format!= null){
            if (format.equals(".jpg") || format.equals(".png")){
                Glide.with(context)
                    .load(commentModel.profilpicUrl)
                    .into(holder.circleImageViewComment)
            }
        }
        holder.textViewCommentUser.text = commentModel.email
        holder.textViewCommentContent.text = commentModel.commentText
        holder.textViewCommentDate.text = DateFormat.getDateInstance(DateFormat.LONG,Locale("in","ID")).format(date)

    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val circleImageViewComment = view.circleImageViewComment
        val textViewCommentUser = view.textViewCommentUser
        val textViewCommentContent = view.textViewCommentContent
        val textViewCommentDate = view.textViewCommentDate


    }

}