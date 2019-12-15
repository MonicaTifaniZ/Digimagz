package com.monicatifanyz.digimagz.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.monicatifanyz.digimagz.R
import com.monicatifanyz.digimagz.model.StoryModel
import com.monicatifanyz.digimagz.view.activity.DetailStoryActivity
import kotlinx.android.synthetic.main.list_cover_story.view.*

class RecyclerViewStoryAdapter(
    var storyModelArrayList: ArrayList<StoryModel>
) : RecyclerView.Adapter<RecyclerViewStoryAdapter.ViewHolder>() {

    private val  INTENT_PARAM_KEY_STORY_DATA : String = "INTENT_PARAM_KEY_STORY_DATA"
    private lateinit var newsImage : String

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val imageViewStory = view.imageViewStory
        val textViewTitle = view.textViewTitle

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val view:View = LayoutInflater.from(parent.context).inflate(R.layout.list_cover_story, parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
       return storyModelArrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val storyModel : StoryModel = storyModelArrayList.get(position)
        Glide.with(holder.itemView.context)
            .load(newsImage)
            .into(holder.imageViewStory)

        holder.textViewTitle.text = storyModel.titleCoverStory
        holder.itemView.setOnClickListener {
            var intent :Intent = Intent(it.context, DetailStoryActivity::class.java)
            intent.putExtra(INTENT_PARAM_KEY_STORY_DATA,storyModel)
            it.context.startActivity(intent)
        }
    }
}