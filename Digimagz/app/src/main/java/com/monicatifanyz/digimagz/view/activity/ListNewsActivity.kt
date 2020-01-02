package com.monicatifanyz.digimagz.view.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.monicatifanyz.digimagz.R
import com.monicatifanyz.digimagz.adapter.RecyclerViewNewsAdapter
import com.monicatifanyz.digimagz.api.InitRetrofit
import com.monicatifanyz.digimagz.model.NewsModel
import kotlinx.android.synthetic.main.activity_list_news.*
import java.util.*

class ListNewsActivity : AppCompatActivity() {


    private lateinit var initRetrofit:InitRetrofit
    private lateinit var params : String
    private var materialToolbar : MaterialToolbar? = null
    private lateinit var recyclerView:RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_news)

        params = intent.getStringExtra("params")

        setSupportActionBar(materialToolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        setTitle("")

        initRetrofit = InitRetrofit()

        recyclerView()

    }

    private fun recyclerView(){
        if(params.equals("trending")){
            initRetrofit.getNewsTrendingFromApi()
            initRetrofit.setOnRetrofitSuccess(object : InitRetrofit.OnRetrofitSuccess{
                override fun onSuccessGetData(arrayList: ArrayList<*>?) {
                    if (arrayList != null) {
                        if(arrayList.isNotEmpty()){
                            Log.i("Size", arrayList.size.toString())
                            showRecyclerListViewNews(arrayList as ArrayList<NewsModel>)

                        } else{
                            frameLayoutEmpty!!.visibility = View.VISIBLE
                            recyclerViewNews!!.visibility = View.GONE
                            shimmerFrameLayoutNewsListNews.stopShimmer()
                            shimmerFrameLayoutNewsListNews.visibility = View.GONE
                            Log.i("Size", arrayList.size.toString())
                        }
                    }
                }

            })
        } else {
            initRetrofit.getNewsFromApiWithParams(params)
            initRetrofit.setOnRetrofitSuccess(object:InitRetrofit.OnRetrofitSuccess{
                override fun onSuccessGetData(arrayList: ArrayList<*>?) {
                    if (arrayList != null) {
                        if(arrayList.isNotEmpty()){
                            Log.i("Size", arrayList.size.toString())
                            showRecyclerListViewNews(arrayList as ArrayList<NewsModel>)
                        } else{
                            frameLayoutEmpty.visibility = View.VISIBLE
                            recyclerViewNews.visibility = View.GONE
                            shimmerFrameLayoutNewsListNews.stopShimmer()
                            shimmerFrameLayoutNewsListNews.visibility = View.GONE
                            Log.i("Size", arrayList.size.toString())
                        }
                    }
                }
            })
        }
    }

    private fun showRecyclerListViewNews(newsModelArrayList:ArrayList<NewsModel>){
        recyclerViewNews.setHasFixedSize(true)
        recyclerViewNews.layoutManager = LinearLayoutManager(this)
        val recyclerViewNewsAdapter = RecyclerViewNewsAdapter(newsModelArrayList, newsModelArrayList.size)
        recyclerViewNews.adapter = recyclerViewNewsAdapter
        shimmerFrameLayoutNewsListNews.stopShimmer()
        shimmerFrameLayoutNewsListNews.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        recyclerView()
        shimmerFrameLayoutNewsListNews.startShimmer()
    }

    override fun onStop() {
        shimmerFrameLayoutNewsListNews.stopShimmer()
        super.onStop()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
