package com.monicatifanyz.digimagz.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.appbar.MaterialToolbar
import com.monicatifanyz.digimagz.R
import com.monicatifanyz.digimagz.adapter.RecyclerViewNewsAdapter
import com.monicatifanyz.digimagz.api.InitRetrofit
import com.monicatifanyz.digimagz.model.NewsModel
import kotlinx.android.synthetic.main.activity_list_news.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_search.shimmerFrameLayoutNews
import java.util.ArrayList

class ListNewsActivity : AppCompatActivity() {


    private lateinit var initRetrofit:InitRetrofit
    private var params : String = ""
    private var materialToolbar : MaterialToolbar? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_news)

        params = intent.getStringExtra("params")

        materialToolbar = findViewById(R.id.materialToolbar)
        setSupportActionBar(materialToolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        setTitle("")

        initRetrofit = InitRetrofit()

        recyclerView()

    }

    private fun recyclerView(){
        if(!params.contentEquals("trending")){
            initRetrofit.getNewsFromApiWithParam(params)
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

        } else{
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
                            shimmerFrameLayoutNews.stopShimmer()
                            shimmerFrameLayoutNews.visibility = View.GONE
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
        var recyclerViewNewsAdapter: RecyclerViewNewsAdapter = RecyclerViewNewsAdapter(newsModelArrayList, newsModelArrayList.size)
        recyclerViewNews.adapter = recyclerViewNewsAdapter

        //recyclerViewNews!!.setHasFixedSize(true)
        //recyclerViewNews!!.layoutManager = LinearLayoutManager(this)
        //var recyclerViewNewsAdapter: RecyclerViewNewsAdapter = RecyclerViewNewsAdapter(newsModelArrayList, newsModelArrayList.size)
        //recyclerViewNews!!.adapter = recyclerViewNewsAdapter
    }
}
