package com.monicatifanyz.digimagz.adapter

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.monicatifanyz.digimagz.R
import com.monicatifanyz.digimagz.model.VideoModel
import com.monicatifanyz.digimagz.view.activity.DetailVideoActivity
import kotlinx.android.synthetic.main.list_news.view.*
import kotlinx.android.synthetic.main.list_news.view.textViewDate
import kotlinx.android.synthetic.main.list_news.view.textViewTitle
import kotlinx.android.synthetic.main.list_video.view.*
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class RecyclerViewVideoAdapter (
    var videoModelArrayList: ArrayList<VideoModel>,
    var context: Context
) : RecyclerView.Adapter<RecyclerViewVideoAdapter.ViewHolder>(){

    private lateinit var simpleDateFormat: SimpleDateFormat
    private lateinit var date: Date

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewTitle  = view.textViewTitle
        val textViewDate  = view.textViewDate
        val youTubePlayerViewHolder = view.youTubePlayerView
        val frameLayoutVideo = view.frameLayoutVideo
        val imageThumbnailVideo = view.imageViewThumbnailVideo
        val materialButtonYoutube = view.materialButtonYoutube


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view:View= LayoutInflater.from(parent.context).inflate(R.layout.list_video, parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return videoModelArrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val  dataModel : VideoModel = videoModelArrayList.get(position)

        simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        try{
            date = simpleDateFormat.parse(dataModel.datePublished.substring(0,10))

        } catch (e : ParseException){
            e.printStackTrace()
        }
        Log.e("date", dataModel.datePublished)

        Glide.with(context)
            .load(dataModel.urlMediumThumbnail)
            .into(holder.imageThumbnailVideo)

        holder.textViewTitle.text = dataModel.title
        holder.textViewDate.text = DateFormat.getDateInstance(DateFormat.LONG, Locale("in", "ID")).format(date)

        holder.frameLayoutVideo.setOnClickListener {
            var intent:Intent = Intent(it.context, DetailVideoActivity::class.java)
            intent.putExtra("youtubeId", dataModel.idVideo)
            it.context.startActivity(intent)
        }

        holder.materialButtonYoutube.setOnClickListener {
            var appIntent:Intent = Intent(Intent.ACTION_VIEW , Uri.parse("vnd.youtube:" + dataModel.idVideo))
            var webIntent: Intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + dataModel.idVideo))

            try {
                context.startActivity(appIntent)

            }catch (ex : ActivityNotFoundException){
                context.startActivity(webIntent)
            }
        }
    }



}
