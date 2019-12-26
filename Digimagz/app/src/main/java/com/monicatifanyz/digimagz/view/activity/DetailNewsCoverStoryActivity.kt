package com.monicatifanyz.digimagz.view.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
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
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.monicatifanyz.digimagz.Constant
import com.monicatifanyz.digimagz.R
import com.monicatifanyz.digimagz.adapter.ImageSliderGalleryAdapter
import com.monicatifanyz.digimagz.adapter.RecyclerViewCommentAdapter
import com.monicatifanyz.digimagz.adapter.RecyclerViewNewsAdapter
import com.monicatifanyz.digimagz.adapter.RecyclerViewNewsCoverStoryAdapter
import com.monicatifanyz.digimagz.api.InitRetrofit
import com.monicatifanyz.digimagz.model.CommentModel
import com.monicatifanyz.digimagz.model.NewsCoverStoryModel
import com.monicatifanyz.digimagz.model.NewsModel
import com.monicatifanyz.digimagz.model.UserModel
import com.viewpagerindicator.CirclePageIndicator
import kotlinx.android.synthetic.main.activity_detail_news.*
import kotlinx.android.synthetic.main.activity_detail_news.view.*
import kotlinx.android.synthetic.main.activity_detail_story.*
import kotlinx.android.synthetic.main.activity_detail_story.materialToolbar
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlinx.android.synthetic.main.activity_detail_news.textViewTitle as textViewTitle1
import kotlinx.android.synthetic.main.activity_detail_story.imageViewCover as imageViewCover1
import kotlinx.android.synthetic.main.activity_detail_story.textViewContent as textViewContent1

class DetailNewsCoverStoryActivity : AppCompatActivity() {

    private val firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser

    private lateinit var webViewDetailNews: WebView
    private lateinit var recyclerViewComment: RecyclerView
    private lateinit var recyclerViewNews: RecyclerView

    private lateinit var linearLayoutShare: LinearLayout

    private lateinit var newsCoverStoryModel: NewsCoverStoryModel


    private lateinit var simpleDateFormat: SimpleDateFormat
    private lateinit var date: Date
    private lateinit var newsImage: String
    private lateinit var dataHtml: String

    private lateinit var initRetrofit: InitRetrofit
    private lateinit var initRetrofitComment: InitRetrofit
    private lateinit var initRetrofitNews: InitRetrofit
    private lateinit var initRetrofitLike: InitRetrofit
    private lateinit var initRetrofitView: InitRetrofit
    private lateinit var initRetrofitShare: InitRetrofit
    private lateinit var initRetrofitUser: InitRetrofit
    private lateinit var recyclerViewCommentAdapter: RecyclerViewCommentAdapter
    private var userModels: ArrayList<UserModel> = ArrayList()

    private lateinit var mPager: ViewPager
    private lateinit var relativeLayoutSlider: RelativeLayout
    private var currentPage: Int = 0
    private var NUM_PAGES: Int = 0

    private lateinit var indicator: CirclePageIndicator

    private var swiper: Handler = Handler()
    private var swipeRunnable: Runnable = object : Runnable {
        override fun run() {
            if (currentPage == NUM_PAGES) {
                currentPage = 0
            }
            mPager.setCurrentItem(currentPage++, true)

            swiper.postDelayed(this, 3000)
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_news_cover_story)

        simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        newsCoverStoryModel = intent.getSerializableExtra("data") as NewsCoverStoryModel

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
            date = simpleDateFormat.parse(newsCoverStoryModel.dateNews)

        } catch (e: ParseException) {
            e.printStackTrace()
        }

        if (newsCoverStoryModel.newsImage != null) {
            if (newsCoverStoryModel.newsImage.isNotEmpty()) {
                if (newsCoverStoryModel.nameCategory.equals("Berita")) {
                    newsImage = Constant().URL_IMAGE_NEWS + newsCoverStoryModel.newsImage.get(0)
                } else if (newsCoverStoryModel.nameCategory.equals("Artikel")) {
                    newsImage = Constant().URL_IMAGE_NEWS + newsCoverStoryModel.newsImage.get(0)
                } else if (newsCoverStoryModel.nameCategory.equals("Galeri")) {
                    newsImage =
                        Constant().URL_IMAGE_GALERY + newsCoverStoryModel.idNews + "/" + newsCoverStoryModel.idNews + "/" + newsCoverStoryModel.newsImage.get(
                            0
                        )
                    relativeLayoutSlider.visibility = View.VISIBLE
                    imageViewCover.visibility = View.GONE
                    showSlider(newsCoverStoryModel.newsImage, newsCoverStoryModel.idNews)
                }
                Glide.with(this)
                    .load(newsImage)
                    .into(imageViewCover)

            }
        }
        if (newsCoverStoryModel.titleNews != null) {
            textViewTitle.text = newsCoverStoryModel.titleNews
        }
        if (date != null) {
            textViewDate.text =
                DateFormat.getDateInstance(DateFormat.LONG, Locale("in", "ID")).format(date)
        }

        if (newsCoverStoryModel.contentNews != null) {

            dataHtml =
                "<html><head><meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"></head>"
            dataHtml =
                dataHtml + "<body width=\"100%\" height=\"auto\">" + newsCoverStoryModel.contentNews + "</body></html>"
            webViewDetailNews.loadDataWithBaseURL(null, dataHtml, "text/html", "UTF-8", null)
            webViewDetailNews.webViewClient = WebViewClient()

            textViewContent.text = Html.fromHtml(newsCoverStoryModel.contentNews.toString())

        }

        if (newsCoverStoryModel.nameCategory != null) {
            textViewCategory.text = newsCoverStoryModel.nameCategory

        }

        textViewLike.text = newsCoverStoryModel.likes.toString()

        if (newsCoverStoryModel.editor != null) {
            textViewEditor.text = newsCoverStoryModel.editor

        }
        if (newsCoverStoryModel.verificator != null) {
            textViewVerificator.text = newsCoverStoryModel.verificator

        }
        setRecyclerView()

        if (firebaseUser != null) {
            imageButtonLike.isEnabled = true
            imageButtonDislike.isEnabled = true
            imageButtonSendComment.isEnabled = true
            textInputEditTextComment.isEnabled = true

            initRetrofitUser.getUserFromApi(firebaseUser.email.toString())
            initRetrofitUser.setOnRetrofitSuccess(object : InitRetrofit.OnRetrofitSuccess {
                override fun onSuccessGetData(arrayList: ArrayList<*>?) {
                    if (arrayList != null) {
                        if (!arrayList.isEmpty()) {
                            userModels.addAll(arrayList as ArrayList<UserModel>)

                        }
                    }
                }

            })

            initRetrofitView.postViewToApi(
                newsCoverStoryModel.idNews,
                firebaseUser.email.toString()
            )
            initRetrofitLike.getLikeFromApi(
                newsCoverStoryModel.idNews,
                firebaseUser.email.toString()
            )
            initRetrofitLike.setOnRetrofitSuccess(object : InitRetrofit.OnRetrofitSuccess {
                override fun onSuccessGetData(arrayList: java.util.ArrayList<*>?) {
                    if (arrayList != null) {
                        if (arrayList.get(0).equals("Yes")) {
                            imageButtonDislike.visibility = View.VISIBLE
                            imageButtonLike.visibility = View.GONE
                        } else if (arrayList.get(0).equals("No")) {
                            imageButtonDislike.visibility = View.GONE
                            imageButtonLike.visibility = View.VISIBLE
                        }
                    }
                }

            })


            imageButtonLike.setOnClickListener {
                imageButtonLike.visibility = View.GONE
                imageButtonDislike.visibility = View.VISIBLE
                initRetrofit.postLikeToApi(
                    newsCoverStoryModel.idNews,
                    firebaseUser.email.toString()
                )
                newsCoverStoryModel.likes = newsCoverStoryModel.likes + 1
                textViewLike.text = newsCoverStoryModel.likes.toString()

            }

            imageButtonDislike.setOnClickListener({
                imageButtonLike.visibility = View.VISIBLE
                imageButtonDislike.visibility = View.GONE
                initRetrofit.deleteLikeFromApi(
                    newsCoverStoryModel.idNews,
                    firebaseUser.email.toString()
                )
                newsCoverStoryModel.likes = newsCoverStoryModel.likes - 1
                textViewLike.text = newsCoverStoryModel.likes.toString()

            })

            imageButtonSendComment.setOnClickListener {
                if (textInputEditTextComment.length() > 0) {
                    initRetrofit.postCommentToApi(newsCoverStoryModel.idNews, firebaseUser.email.toString(), textInputEditTextComment.text.toString())

                    var calendar: Calendar = Calendar.getInstance()
                    var dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

                    textViewCountComment.textInputEditTextComment.text.toString() + 1
                    textInputEditTextComment.text

                } else {
                    Toast.makeText(this, "Harap isi komentar dengan benar", Toast.LENGTH_SHORT)
                        .show()
                }
            }

        } else {
            imageButtonLike.isEnabled = false
            imageButtonDislike.isEnabled = false
            imageButtonSendComment.isEnabled = false
            textInputEditTextComment.isEnabled = false

            linearLayoutShare.setOnClickListener {
                openShare(newsCoverStoryModel)
            }
        }

    }

    fun setRecyclerView() {
        swiper.removeCallbacks(swipeRunnable)

        initRetrofitComment.getCommentFromApi(newsCoverStoryModel.idNews)
        initRetrofitComment.setOnRetrofitSuccess(object : InitRetrofit.OnRetrofitSuccess {
            override fun onSuccessGetData(arrayList: java.util.ArrayList<*>?) {
                if (arrayList != null) {
                    Log.i("Size", arrayList.size.toString())
                    textViewCountComment.text = arrayList.size.toString()

                    lateinit var commentModels: ArrayList<CommentModel>
                    for (i in arrayList.size - 1..0) {
                        commentModels.add(arrayList.get(i) as CommentModel)
                    }

                    showRecyclerListViewComment(arrayList as ArrayList<CommentModel>)
                }
            }

        })

        initRetrofitNews.getNewsRelatedFromApi(newsCoverStoryModel.idNews)
        initRetrofitNews.setOnRetrofitSuccess(object : InitRetrofit.OnRetrofitSuccess {
            override fun onSuccessGetData(arrayList: java.util.ArrayList<*>?) {
                if (arrayList != null) {
                    if (arrayList.isNotEmpty()) {
                        Log.i("Size", arrayList.size.toString())
                        showRecyclerListViewNews(arrayList as ArrayList<NewsModel>)

                    } else {
                        Log.i("Size", arrayList.size.toString())

                    }
                }
            }

        })
    }

    fun showRecyclerListViewComment(commentModelArrayList: ArrayList<CommentModel>) {
        recyclerViewComment.layoutManager = LinearLayoutManager(this)
        recyclerViewCommentAdapter =
            RecyclerViewCommentAdapter(commentModelArrayList, applicationContext)
        recyclerViewComment.adapter = recyclerViewCommentAdapter

    }

    fun showRecyclerListViewNews(newsModelArrayList: ArrayList<NewsModel>) {
        recyclerViewNews.setHasFixedSize(true)
        recyclerViewNews.layoutManager = LinearLayoutManager(this)
        var recyclerViewNewsAdapter: RecyclerViewNewsAdapter
        if (newsModelArrayList.size >= 3) {
            recyclerViewNewsAdapter = RecyclerViewNewsAdapter(newsModelArrayList, 3)

        } else {
            recyclerViewNewsAdapter =
                RecyclerViewNewsAdapter(newsModelArrayList, newsModelArrayList.size)
        }
        recyclerViewNews.adapter = recyclerViewNewsAdapter
    }

    fun openShare(model: NewsCoverStoryModel) {
        var myIntent: Intent = Intent(Intent.ACTION_SEND)
        myIntent.setType("text/plain")
        var shareBody: String =
            model.titleNews + "\n" + "http://digimagz.kristomoyo.com/news/view/" + model.idNews
        var shareSub: String = "Digimagz"
        myIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub)
        myIntent.putExtra(Intent.EXTRA_TEXT, shareBody)

        startActivity(Intent.createChooser(myIntent, "Share \"Digimagz\" via"))
    }

    fun showSlider(newsModelArrayList: ArrayList<String>, idNews: String) {
        if (newsModelArrayList.size > 0) {
            mPager.adapter = ImageSliderGalleryAdapter(newsModelArrayList, idNews)
            indicator.setViewPager(mPager)

            val density: Float = resources.displayMetrics.density

            indicator.radius = (5 * density)

            NUM_PAGES = newsModelArrayList.size

            swiper.postDelayed(swipeRunnable, 3000)

            indicator.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
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

            relativeLayoutSlider.visibility = View.VISIBLE


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
            if (firebaseUser != null){
                initRetrofitShare.postShareToApi(newsCoverStoryModel.idNews, firebaseUser.email)
            }
        }
    }
}
