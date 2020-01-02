package com.monicatifanyz.digimagz.view.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Html
import android.util.Log
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.os.postDelayed
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.monicatifanyz.digimagz.Constant
import com.monicatifanyz.digimagz.R
import com.monicatifanyz.digimagz.adapter.ImageSliderAdapter
import com.monicatifanyz.digimagz.adapter.ImageSliderGalleryAdapter
import com.monicatifanyz.digimagz.adapter.RecyclerViewCommentAdapter
import com.monicatifanyz.digimagz.adapter.RecyclerViewNewsAdapter
import com.monicatifanyz.digimagz.api.InitRetrofit
import com.monicatifanyz.digimagz.model.CommentModel
import com.monicatifanyz.digimagz.model.UserModel
import com.viewpagerindicator.CirclePageIndicator
import kotlinx.android.synthetic.main.activity_detail_news.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.materialToolbar
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import com.monicatifanyz.digimagz.model.NewsModel as NewsModel

class DetailNewsActivity : AppCompatActivity(){

    private val firebaseUser:FirebaseUser? = FirebaseAuth.getInstance().currentUser

    private lateinit var newsModel : NewsModel

    private lateinit var simpleDateFormat:SimpleDateFormat
    private lateinit var date: Date;
    private lateinit var newsImage : String
    private lateinit var dataHtml : String

    private var currentPage: Int = 0
    private var NUM_PAGES: Int = 0

    private lateinit var initRetrofit: InitRetrofit
    private lateinit var initRetrofitComment: InitRetrofit
    private lateinit var initRetrofitNews: InitRetrofit
    private lateinit var initRetrofitLike: InitRetrofit
    private lateinit var initRetrofitView: InitRetrofit
    private lateinit var initRetrofitShare: InitRetrofit
    private lateinit var initRetrofitUser: InitRetrofit
    private lateinit var recyclerViewCommentAdapter : RecyclerViewCommentAdapter
    private var userModels:ArrayList<UserModel> = ArrayList()

    private var swiper : Handler = Handler()
    private var swipeRunnable : Runnable = object : Runnable {
        override fun run() {
            if (currentPage == NUM_PAGES){
                currentPage = 0
            }
            pagerSlider.setCurrentItem(currentPage++, true)

            swiper.postDelayed(this, 3000)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_news)

        simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        newsModel = intent.getSerializableExtra("data") as NewsModel

        setSupportActionBar(materialToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setTitle("")

        initRetrofit = InitRetrofit()
        initRetrofitComment = InitRetrofit()
        initRetrofitNews = InitRetrofit()
        initRetrofitLike = InitRetrofit()
        initRetrofitView = InitRetrofit()
        initRetrofitShare = InitRetrofit()
        initRetrofitUser = InitRetrofit()

        webViewDetailNews.settings.javaScriptEnabled = true
        webViewDetailNews.settings.javaScriptCanOpenWindowsAutomatically = true
        webViewDetailNews.settings.pluginState = WebSettings.PluginState.ON
        webViewDetailNews.settings.setSupportMultipleWindows(true)
        webViewDetailNews.webChromeClient = WebChromeClient()
        webViewDetailNews.isHorizontalScrollBarEnabled = false
        webViewDetailNews.settings.loadWithOverviewMode = true
        webViewDetailNews.settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        webViewDetailNews.settings.builtInZoomControls = false

        try {
            date = simpleDateFormat.parse(newsModel.dateNews)

        } catch (e : ParseException){
            e.printStackTrace()
        }

        if (newsModel.newsImage != null){
            if (newsModel.newsImage.isNotEmpty()){
                if (newsModel.nameCategory.equals("Berita")){
                    newsImage = Constant().URL_IMAGE_NEWS + newsModel.newsImage.get(0)
                } else if (newsModel.nameCategory.equals("Artikel")){
                    newsImage = Constant().URL_IMAGE_NEWS + newsModel.newsImage.get(0)
                } else if (newsModel.nameCategory.equals("Galeri")){
                    newsImage = Constant().URL_IMAGE_GALLERY + newsModel.idNews + "/"  + newsModel.idNews + "/" + newsModel.newsImage.get(0)
                    relativeLayoutSlider.visibility = View.VISIBLE
                    imageViewCover.visibility = View.GONE
                    showSlider(newsModel.newsImage, newsModel.idNews)
                }
                Glide.with(this)
                    .load(newsImage)
                    .into(imageViewCover)
            }
        }

        if (newsModel.titleNews != null){
            textViewTitle.text = newsModel.titleNews
        }

        if (date != null){
            textViewDate.text = DateFormat.getDateInstance(DateFormat.LONG, Locale("in", "ID")).format(date)
        }

        if (newsModel.contentNews != null){
            dataHtml = "<html><head><meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"></head>"
            dataHtml =  dataHtml + "<body width=\"100%\" height=\"auto\">" + newsModel.contentNews  + "</body></html>"

            webViewDetailNews.loadDataWithBaseURL(null, dataHtml,"text/html", "UTF-8", null )
            webViewDetailNews.webViewClient = WebViewClient()

            textViewContent.text = Html.fromHtml(newsModel.contentNews)
        }
        if (newsModel.nameCategory != null){
            textViewCategory.text = newsModel.nameCategory
        }

        textViewLike.text = newsModel.likes.toString()
        if (newsModel.editor != null){
            textViewEditor.text = newsModel.verificator
        }

        setRecyclerView()

        if (firebaseUser != null){
            imageButtonLike.isEnabled = true
            imageButtonDislike.isEnabled = true
            imageButtonSendComment.isEnabled = true
            textInputEditTextComment.isEnabled = true

            initRetrofitUser.getUserFromApi(firebaseUser.email.toString())
            initRetrofitUser.setOnRetrofitSuccess(object : InitRetrofit.OnRetrofitSuccess{
                override fun onSuccessGetData(arrayList: ArrayList<*>?) {
                    if (arrayList != null) {
                        if (arrayList.isNotEmpty()){
                            userModels.addAll(arrayList as ArrayList<UserModel>)
                        }
                    }
                }

            })

            initRetrofitView.postViewToApi(newsModel.idNews, firebaseUser.email!!)

            initRetrofitLike.getLikeFromApi(newsModel.idNews, firebaseUser.email.toString())
            initRetrofitLike.setOnRetrofitSuccess(object : InitRetrofit.OnRetrofitSuccess{
                override fun onSuccessGetData(arrayList: ArrayList<*>?) {
                    if (arrayList != null) {
                        if (arrayList.get(0).equals("Yes")){
                            imageButtonDislike.visibility = View.VISIBLE
                            imageButtonLike.visibility = View.GONE
                        } else if (arrayList.get(0).equals("No")){
                            imageButtonDislike.visibility = View.GONE
                            imageButtonLike.visibility = View.VISIBLE
                        }
                    }
                }

            })

            imageButtonLike.setOnClickListener {
                imageButtonLike.visibility = View.GONE
                imageButtonDislike.visibility = View.VISIBLE
                initRetrofit.postLikeToApi(newsModel.idNews, firebaseUser.email.toString())
                newsModel.likes = newsModel.likes.toString().toInt() + 1
                newsModel.checkLike = 1
                textViewLike.text = newsModel.likes.toString()
            }

            imageButtonDislike.setOnClickListener{
                imageButtonLike.visibility = View.VISIBLE
                imageButtonDislike.visibility = View.GONE
                initRetrofit.deleteLikeFromApi(newsModel.idNews, firebaseUser.email.toString())
                newsModel.likes = newsModel.likes.toString().toInt() - 1
                newsModel.checkLike = 2
                textViewLike.setText(newsModel.likes.toString())
            }

            imageButtonSendComment.setOnClickListener {
                if (textInputEditTextComment.length() > 0){
                    initRetrofit.postCommentToApi(newsModel.idNews, firebaseUser.email.toString(), textInputEditTextComment.text.toString())
                    textViewCountComment.text = (textViewCountComment.text.toString().toInt() + 1).toString()
                    textInputEditTextComment.text?.clear()
                } else{
                    Toast.makeText(this, "Harap isi komentar dengan benar" , Toast.LENGTH_SHORT).show()
                }
            }


        } else{
            imageButtonLike.isEnabled = false
            imageButtonDislike.isEnabled = false
            imageButtonSendComment.isEnabled = false
            textInputEditTextComment.isEnabled = false
        }

        linearLayoutShare.setOnClickListener {
            openShare(newsModel)
        }

    }

    fun setRecyclerView(){
        initRetrofitComment.getCommentFromApi(newsModel.idNews)
        initRetrofitComment.setOnRetrofitSuccess(object : InitRetrofit.OnRetrofitSuccess{
            override fun onSuccessGetData(arrayList: ArrayList<*>?) {
                if (arrayList != null) {
                    if (arrayList.isNotEmpty()) {
                        Log.i("Size", arrayList.size.toString())
                        textViewCountComment.text = arrayList.size.toString()
                        showRecyclerListViewComment(arrayList as ArrayList<CommentModel>)
                    }
                }
            }
        })

        initRetrofitNews.getNewsRelatedFromApi(newsModel.idNews)
        initRetrofitNews.setOnRetrofitSuccess(object: InitRetrofit.OnRetrofitSuccess{
            override fun onSuccessGetData(arrayList: java.util.ArrayList<*>?) {
                if (arrayList != null) {
                    if (arrayList.isNotEmpty()){
                        Log.i("Size", arrayList.size.toString())
                        showRecyclerListViewNews(arrayList as ArrayList<NewsModel>)

                    } else{
                        Log.i("Size", arrayList.size.toString())
                    }
                }
            }
        })
    }

    fun showRecyclerListViewComment(commentModelArrayList: ArrayList<CommentModel>){
        Log.e("showRecyclerListView", commentModelArrayList.size.toString())
        recyclerViewComment.setHasFixedSize(true)
        recyclerViewComment.layoutManager =  LinearLayoutManager(this)
        recyclerViewCommentAdapter = RecyclerViewCommentAdapter(commentModelArrayList, applicationContext)
        recyclerViewComment.adapter = recyclerViewCommentAdapter
    }

    fun showRecyclerListViewNews(newsModelArrayList: ArrayList<NewsModel>){
        recyclerViewNews.setHasFixedSize(true)
        recyclerViewNews.layoutManager = LinearLayoutManager(this)
        var recyclerViewNewsAdapter : RecyclerViewNewsAdapter
        if (newsModelArrayList.size >= 3){
            recyclerViewNewsAdapter = RecyclerViewNewsAdapter(newsModelArrayList , 3)
        } else{
            recyclerViewNewsAdapter = RecyclerViewNewsAdapter(newsModelArrayList, newsModelArrayList.size)
        }

        recyclerViewNews.adapter = recyclerViewNewsAdapter

    }

    fun openShare(model: NewsModel){
        val myIntent = Intent(Intent.ACTION_SEND)
        myIntent.setType("text/plain")
        val shareBody : String = model.titleNews + "\n" + "http://digimagz.kristomoyo.com/news/view/" + model.idNews
        val shareSub = "Digimagz"
        myIntent.putExtra(Intent.EXTRA_SUBJECT , shareSub)
        myIntent.putExtra(Intent.EXTRA_TEXT, shareBody)

        startActivity(Intent.createChooser(myIntent, "Share \"Digimagz\" via"))

    }

    private fun showSlider(newsModelArrayList: ArrayList<String> , idNews :String){
        if(newsModelArrayList.size > 0 ){
            pagerSlider.adapter = ImageSliderGalleryAdapter(newsModelArrayList, idNews)
            indicatorSlider.setViewPager(pagerSlider)

            val density: Float = resources.displayMetrics.density

            indicatorSlider.radius = (5 * density)

            NUM_PAGES = newsModelArrayList.size

            swiper.postDelayed(swipeRunnable,3000)


            indicatorSlider.setOnPageChangeListener(object :ViewPager.OnPageChangeListener{
                override fun onPageScrollStateChanged(state: Int) {

                }

                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    currentPage = position
                    Log.e("pos", currentPage.toString())

                }

                override fun onPageSelected(position: Int) {

                }

            })


        }
    }

    override fun onResume() {
        super.onResume()
        setRecyclerView()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            if (firebaseUser !== null){
                initRetrofitShare.postShareToApi(newsModel.idNews, firebaseUser.email!!)
            }
        }
    }

}

