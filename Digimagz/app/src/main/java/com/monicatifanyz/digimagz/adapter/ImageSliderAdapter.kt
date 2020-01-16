package com.monicatifanyz.digimagz.adapter

import android.content.Intent
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.monicatifanyz.digimagz.Constant
import com.monicatifanyz.digimagz.R
import com.monicatifanyz.digimagz.model.NewsModel
import com.monicatifanyz.digimagz.view.activity.DetailNewsActivity
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ImageSliderAdapter(
    var newsModelArrayList: ArrayList<NewsModel>
): PagerAdapter(){

    val INTENT_PARAM_KEY_NEWS_DATA:String= "INTENT_PARAM_KEY_NEWS_DATA"
    lateinit var simpleDateFormat : SimpleDateFormat
    lateinit var date:Date

    //
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int {
        return newsModelArrayList.size
    }

    override fun instantiateItem(view: ViewGroup, position: Int): Any {
        val imageLayout: View = LayoutInflater.from(view.context).inflate(R.layout.list_image_slider, view, false)
        val newsModel:NewsModel = newsModelArrayList.get(position)

        assert(imageLayout!=null)

        simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        try{
            date = simpleDateFormat.parse(newsModel.dateNews)
        } catch (e : ParseException){
            e.printStackTrace()
        }

        if (newsModel.newsImage != null) {
            var imageUrl = ""
            if (newsModel.nameCategory.equals("Berita")) {
                imageUrl = Constant().URL_IMAGE_NEWS + newsModel.newsImage.get(0)
            } else if (newsModel.nameCategory.equals("Artikel")) {
                imageUrl = Constant().URL_IMAGE_NEWS + newsModel.newsImage.get(0)
            } else if (newsModel.nameCategory.equals("Galeri")) {
                imageUrl =
                    Constant().URL_IMAGE_GALLERY + newsModel.idNews + "/" + newsModel.newsImage.get(0)
            }

            Glide.with(view.context)
                .load(imageUrl)
                .into(imageLayout.findViewById(R.id.imageSlider))
        }

        if (newsModel.titleNews != null) {
            imageLayout.findViewById<TextView>(R.id.textViewTitle).setText(newsModel.titleNews)
        }

        imageLayout.findViewById<TextView>(R.id.textViewDate).text = DateFormat.getDateInstance(DateFormat.LONG, Locale("in", "ID")).format(date)

        imageLayout.setOnClickListener {
                val intent : Intent = Intent(view.context, DetailNewsActivity::class.java)
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