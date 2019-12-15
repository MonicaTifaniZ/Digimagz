package com.monicatifanyz.digimagz.adapter

import android.content.Intent
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.monicatifanyz.digimagz.R
import com.monicatifanyz.digimagz.model.NewsModel
import com.monicatifanyz.digimagz.view.activity.DetailNewsActivity
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ImageSliderAdapter(
    var newsModelArrayList: ArrayList<NewsModel>
): PagerAdapter() {


    val INTENT_PARAM_KEY_NEWS_DATA:String= "INTENT_PARAM_KEY_NEWS_DATA"
    lateinit var simpleDateFormat : SimpleDateFormat
    lateinit var date:Date


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int {
        return newsModelArrayList.size
    }

    override fun instantiateItem(view: ViewGroup, position: Int): Any {
        var imageLayout: View = LayoutInflater.from(view.context).inflate(R.layout.list_image_slider, view, false)
        val newsModel:NewsModel = newsModelArrayList.get(position)

        assert(imageLayout!=null)

        simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        try{
            date = simpleDateFormat.parse(newsModel.dateNews)
        } catch (e : ParseException){
            e.printStackTrace()
        }

        Glide.with(view.context)
            .load("http://digimon.kristomoyo.com/images/news/" + newsModel.newsImage)
            .into(imageLayout.findViewById(R.id.imageSlider))

        imageLayout.findViewById<TextView>(R.id.textViewTitle).text = newsModel.titleNews
        imageLayout.findViewById<TextView>(R.id.textViewDate).text = DateFormat.getDateInstance(DateFormat.LONG, Locale("in", "ID")).format(date)

        imageLayout.setOnClickListener {
                var intent : Intent = Intent(view.context, DetailNewsActivity::class.java)
                intent.putExtra(INTENT_PARAM_KEY_NEWS_DATA, newsModel)
                view.context.startActivity(intent)

              }
        view.addView(imageLayout,0)

        return imageLayout
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
      return view.equals(`object`)
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {

    }

    override fun saveState(): Parcelable? {
        return null
    }

}