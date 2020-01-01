package com.monicatifanyz.digimagz.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.monicatifanyz.digimagz.Constant
import com.monicatifanyz.digimagz.R
import com.monicatifanyz.digimagz.api.InitRetrofit
import com.monicatifanyz.digimagz.model.NewsCoverStoryModel
import com.monicatifanyz.digimagz.view.activity.DetailNewsCoverStoryActivity
import kotlinx.android.synthetic.main.list_news.view.*
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class RecyclerViewNewsCoverStoryAdapter(
    var newsCoverStoryModelArrayList: ArrayList<NewsCoverStoryModel>
    ) : RecyclerView.Adapter<RecyclerViewNewsCoverStoryAdapter.ViewHolder>() {

    private lateinit var simpleDateFormat: SimpleDateFormat
    private lateinit var date: Date
    private lateinit var newsImage : String

    private var firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    val INTENT_PARAM_KEY_NEWS_COVER_STORY_DATA : String = "INTENT_PARAM_KEY_NEWS_DATA"
    private lateinit var initRetrofitLike : InitRetrofit

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val textViewTitle = view.textViewTitle
        val textViewDate = view.textViewDate
        val textViewComment = view.textViewComment
        val textViewLike = view.textViewLike
        val imageViewNews = view.imageViewNews
        val ivNotLike = view.ivNotLike
        val ivLiked = view.ivLiked
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view:View = LayoutInflater.from(parent.context).inflate(R.layout.list_news, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return newsCoverStoryModelArrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val newsCoverStoryModel: NewsCoverStoryModel = newsCoverStoryModelArrayList.get(position)

        if (newsCoverStoryModel.nameCategory.equals("Galeri")) {
            newsImage = Constant().URL_IMAGE_GALLERY + newsCoverStoryModel.idNews + "/" + newsCoverStoryModel.newsImage.get(0)
        } else {
            newsImage = Constant().URL_IMAGE_NEWS + newsCoverStoryModel.newsImage.get(0)
        }

        initRetrofitLike = InitRetrofit()

        simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        try {
            date = simpleDateFormat.parse(newsCoverStoryModel.dateNews)
        } catch (e : ParseException){
            e.printStackTrace()
        }

        if (firebaseUser != null){
            firebaseUser!!.email?.let {
                initRetrofitLike.getLikeFromApi(newsCoverStoryModel.idNews,
                    it
                )
                initRetrofitLike.setOnRetrofitSuccess(object: InitRetrofit.OnRetrofitSuccess{
                    override fun onSuccessGetData(arrayList: java.util.ArrayList<*>?) {
                        if (arrayList != null) {
                            if (arrayList.get(0).equals("Yes")){
                                holder.ivLiked.visibility = View.VISIBLE
                                holder.ivNotLike.visibility = View.GONE
                            } else if (arrayList.get(0).equals("No")){
                                holder.ivLiked.visibility = View.GONE
                                holder.ivNotLike.visibility = View.VISIBLE
                            }
                        }
                    }

                })
            }

            holder.textViewTitle.text = newsCoverStoryModel.titleNews
            holder.textViewLike.text = newsCoverStoryModel.likes.toString()
            holder.textViewComment.text = newsCoverStoryModel.comments.toString()
            holder.textViewDate.text = DateFormat.getDateInstance(DateFormat.SHORT, Locale("in", "ID")).format(date)
            Glide.with(holder.itemView.context)
                .load(newsImage)
                .into(holder.imageViewNews)

            if(newsCoverStoryModel.checkLike == 1){
                holder.ivLiked.visibility = View.VISIBLE
                holder.ivNotLike.visibility =View.GONE
            } else{
                holder.ivLiked.visibility = View.GONE
                holder.ivNotLike.visibility = View.VISIBLE
            }
            holder.itemView.setOnClickListener {
                var intent: Intent = Intent(it.context, DetailNewsCoverStoryActivity::class.java)
                intent.putExtra(INTENT_PARAM_KEY_NEWS_COVER_STORY_DATA, newsCoverStoryModel)
                it.context.startActivity(intent)
            }

        }
    }
}