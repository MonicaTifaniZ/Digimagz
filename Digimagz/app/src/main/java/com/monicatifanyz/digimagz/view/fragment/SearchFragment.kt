package com.monicatifanyz.digimagz.view.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.monicatifanyz.digimagz.R
import com.monicatifanyz.digimagz.adapter.RecyclerViewNewsAdapter
import com.monicatifanyz.digimagz.api.InitRetrofit
import com.monicatifanyz.digimagz.model.NewsModel
import com.monicatifanyz.digimagz.view.activity.ListNewsActivity
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_search.view.*
import java.util.*

class SearchFragment : Fragment() {

    private lateinit var initRetrofit: InitRetrofit

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_search, container, false)
        initRetrofit = InitRetrofit()

        setRecyclerView()

        view.swipeRefreshLayout.setOnRefreshListener {
            val handler = Handler()
            handler.postDelayed({
                setRecyclerView()
                view.swipeRefreshLayout.isRefreshing = false
            }, 2500)
        }

        view.materialButtonSearch.setOnClickListener {
            val intent = Intent(context, ListNewsActivity::class.java)
            intent.putExtra("params", textInputEditTextSearch.text.toString())
            startActivity(intent)
        }

        return view
    }

    private fun setRecyclerView() {
        initRetrofit.getNewsFromApi()
        initRetrofit.setOnRetrofitSuccess(object : InitRetrofit.OnRetrofitSuccess{
            override fun onSuccessGetData(arrayList: ArrayList<*>?) {
                if (arrayList!!.isNotEmpty()) {
                    Log.e("newsData", arrayList.size.toString())
                    showRecyclerViewNews(arrayList as ArrayList<NewsModel>)
                }
            }

        })
    }

    fun showRecyclerViewNews(list: ArrayList<NewsModel>) {
        recyclerViewNews.layoutManager = LinearLayoutManager(context)
        val recyclerViewNewsAdapter = RecyclerViewNewsAdapter(list, list.size)
        recyclerViewNews.adapter = recyclerViewNewsAdapter
        recyclerViewNewsAdapter.notifyDataSetChanged()
        shimmerFrameLayoutNews.stopShimmer()
        shimmerFrameLayoutNews.visibility = View.GONE
    }

    fun scrollUp() {
        nestedScrollViewSearch.smoothScrollTo(0,0)
    }

    override fun onResume() {
        super.onResume()
        setRecyclerView()
        shimmerFrameLayoutNews.startShimmer()
    }

    override fun onPause() {
        shimmerFrameLayoutNews.stopShimmer()
        super.onPause()
    }
}
