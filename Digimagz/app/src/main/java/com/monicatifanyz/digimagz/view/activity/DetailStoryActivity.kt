package com.monicatifanyz.digimagz.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.monicatifanyz.digimagz.Constant
import com.monicatifanyz.digimagz.R
import com.monicatifanyz.digimagz.adapter.RecyclerViewNewsCoverStoryAdapter
import com.monicatifanyz.digimagz.api.InitRetrofit
import com.monicatifanyz.digimagz.model.NewsCoverStoryModel
import com.monicatifanyz.digimagz.model.StoryModel
import kotlinx.android.synthetic.main.activity_detail_story.*
import java.util.ArrayList
import com.monicatifanyz.digimagz.adapter.RecyclerViewStoryAdapter as RecyclerViewStoryAdapter

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var initRetrofit:InitRetrofit
    private lateinit var recyclerViewNews: RecyclerView

    private lateinit var storyModel: StoryModel

    private lateinit var newsImage:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_story)

        storyModel = intent.getSerializableExtra("data") as StoryModel

        setSupportActionBar(materialToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setTitle("")

        initRetrofit = InitRetrofit()
        textViewTitle.text = storyModel.titleCoverStory
        textViewContent.text = Html.fromHtml(storyModel.summary)

        newsImage = Constant().URL_IMAGE_STORY + storyModel.imageCoverStory
        Glide.with(this)
            .load(newsImage)
            .into(imageViewCover)

        setRecyclerView()
        
    }

    fun setRecyclerView(){
        initRetrofit.getNewsCoverStoryFromApi(storyModel.idCoverStory)
        initRetrofit.setOnRetrofitSuccess(object : InitRetrofit.OnRetrofitSuccess{
            override fun onSuccessGetData(arrayList: ArrayList<*>?) {
                if (arrayList != null) {
                    if (arrayList.isNotEmpty()){
                        textViewCountNews.text = arrayList.size.toString() + "Story Pilihan"
                        showRecyclerListViewNews(arrayList as ArrayList<NewsCoverStoryModel>)

                    } else{
                        Log.e("Size", "Empty")
                    }
                }
            }
        })
    }

    fun showRecyclerListViewNews(newsModelArrayList: ArrayList<NewsCoverStoryModel>){
        recyclerViewNews.setHasFixedSize(true)
        recyclerViewNews.layoutManager = LinearLayoutManager(this)
        var recyclerViewNewsAdapter :RecyclerViewNewsCoverStoryAdapter = RecyclerViewNewsCoverStoryAdapter(newsModelArrayList)
        recyclerViewNews.adapter = recyclerViewNewsAdapter

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onResume() {
        super.onResume()
        setRecyclerView()
    }
}
