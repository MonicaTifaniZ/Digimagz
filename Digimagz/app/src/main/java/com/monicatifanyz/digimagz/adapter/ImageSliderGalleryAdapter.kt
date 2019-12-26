package com.monicatifanyz.digimagz.adapter

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.monicatifanyz.digimagz.Constant
import com.monicatifanyz.digimagz.R
import kotlinx.android.synthetic.main.list_image.view.*

class ImageSliderGalleryAdapter(
    var stringArrayList: ArrayList<String>,
    var idNews: String
) : PagerAdapter() {

    val INTENT_PARAM_KEY_NEWS_DATA : String = "INTENT_PARAM_KEY_NEWS_DATA"
    private lateinit var newsImage : String


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
       return view.equals(`object`)
    }

    override fun getCount(): Int {
        return  stringArrayList.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var imageLayout : View = LayoutInflater.from(container.context).inflate(R.layout.list_image, container, false)

        newsImage = Constant().URL_IMAGE_GALERY + idNews + "/" + stringArrayList.get(position)

        Glide.with(container.context)
            .load(newsImage)
            .into(imageLayout.imageViewImage)

        container.addView(imageLayout,0)


        return imageLayout
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {

    }

    override fun saveState(): Parcelable? {
        return null
    }

}